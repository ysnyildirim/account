package com.yil.authentication.user.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.base.MD5Util;
import com.yil.authentication.base.PageDto;
import com.yil.authentication.user.controller.dto.CreateUserDto;
import com.yil.authentication.user.controller.dto.UserDto;
import com.yil.authentication.user.controller.dto.UserPasswordDto;
import com.yil.authentication.user.model.User;
import com.yil.authentication.user.model.UserType;
import com.yil.authentication.user.service.UserService;
import com.yil.authentication.user.service.UserTypeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping(value = "v1/users")
public class UserController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final UserService userService;
    private final UserTypeService userTypeService;

    @Autowired
    public UserController(UserService userService, UserTypeService userTypeService) {
        this.userService = userService;
        this.userTypeService = userTypeService;
    }

    @GetMapping
    public ResponseEntity<PageDto<UserDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<User> userPage = userService.findAllByDeletedTimeIsNull(pageable);
            PageDto<UserDto> pageDto = PageDto.toDto(userPage, UserService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        try {
            User user;
            try {
                user = userService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            UserDto dto = UserService.toDto(user);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/userName={username}")
    public ResponseEntity<UserDto> findByUserName(@PathVariable String userName) {
        try {
            User user;
            try {
                user = userService.findByUserNameAndDeletedTimeIsNull(userName);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            UserDto dto = UserService.toDto(user);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateUserDto dto) {
        try {
            if (userService.existsByUserNameAndDeletedTimeIsNull(dto.getUserName()))
                return ResponseEntity.badRequest().build();
            UserType userType;
            try {
                userType = userTypeService.findById(dto.getUserTypeId());
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            String hashPassword = MD5Util.encode(dto.getPassword());
            User user = User.builder()
                    .userName(dto.getUserName())
                    .password(hashPassword)
                    .userTypeId(userType.getId())
                    .enabled(dto.getEnabled())
                    .locked(false)
                    .build();
            user.setCreatedUserId(authenticatedUserId);
            user.setCreatedTime(new Date());
            user = userService.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @Valid @PathVariable Long id) {
        try {
            User user;
            try {
                user = userService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            user.setDeletedUserId(authenticatedUserId);
            user.setDeletedTime(new Date());
            userService.save(user);
            return ResponseEntity.ok("User deleted.");
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity lock(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                               @PathVariable Long id) {
        try {
            User user;
            try {
                user = userService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            user.setLocked(true);
            userService.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}/unlock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity unlock(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        try {
            User user;
            try {
                user = userService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            user.setLocked(false);
            userService.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity active(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        try {
            User user;
            try {
                user = userService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            user.setEnabled(true);
            userService.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}/inactive")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity inactive(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                   @PathVariable Long id) {
        try {
            User user;
            try {
                user = userService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            user.setEnabled(false);
            userService.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity password(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                   @PathVariable Long id,
                                   @RequestBody UserPasswordDto dto) {
        try {
            if (dto.getNewPassword() == null || dto.getCurrentPassword() == null)
                return ResponseEntity.badRequest().build();
            User user;
            try {
                user = userService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            String currentPassword = MD5Util.encode(dto.getCurrentPassword());
            if (user.getPassword().equals(currentPassword))
                return ResponseEntity.badRequest().build();
            String newPassword = MD5Util.encode(dto.getNewPassword());
            user.setPassword(newPassword);
            user.setLastPasswordChangeTime(new Date());
            userService.save(user);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
