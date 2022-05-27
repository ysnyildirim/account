package com.yil.authentication.user.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.base.MD5Util;
import com.yil.authentication.base.PageDto;
import com.yil.authentication.exception.UserNameCannotBeUsedException;
import com.yil.authentication.user.dto.CreateUserDto;
import com.yil.authentication.user.dto.UserDto;
import com.yil.authentication.user.dto.UserPasswordDto;
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

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RestController
@RequestMapping(value = "/api/account/v1/users")
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
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.findAllByDeletedTimeIsNull(pageable);
        PageDto<UserDto> pageDto = PageDto.toDto(userPage, UserService::toDto);
        return ResponseEntity.ok(pageDto);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        UserDto dto = UserService.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/userName={username}")
    public ResponseEntity<UserDto> findByUserName(@PathVariable String userName) {
        User user = userService.findByUserNameAndDeletedTimeIsNull(userName);
        UserDto dto = UserService.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                          @Valid @RequestBody CreateUserDto dto) {
        if (userService.existsByUserName(dto.getUserName()))
            throw new UserNameCannotBeUsedException();
        UserType userType = userTypeService.findById(dto.getUserTypeId());
        String hashPassword = null;
        try {
            hashPassword = MD5Util.encode(dto.getPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setPassword(hashPassword);
        user.setUserTypeId(userType.getId());
        user.setEnabled(dto.getEnabled());
        user.setLocked(dto.getLocked());
        user.setMail(dto.getMail());
        user.setPersonId(dto.getPersonId());
        user.setCreatedUserId(authenticatedUserId);
        user.setCreatedTime(new Date());
        user = userService.save(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @PathVariable Long id) {
        User user = userService.findByIdAndDeletedTimeIsNull(id);
        user.setDeletedUserId(authenticatedUserId);
        user.setDeletedTime(new Date());
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity lock(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                               @PathVariable Long id) {
        User user = userService.findById(id);
        user.setLocked(true);
        userService.save(user);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/unlock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity unlock(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        User user = userService.findById(id);
        user.setLocked(false);
        userService.save(user);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity active(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        User user = userService.findById(id);
        user.setEnabled(true);
        userService.save(user);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}/inactive")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity inactive(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                   @PathVariable Long id) {
        User user = userService.findById(id);
        user.setEnabled(false);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity password(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                   @PathVariable Long id,
                                   @RequestBody UserPasswordDto dto) {
        User user = userService.findById(id);
        String currentPassword = null;
        try {
            currentPassword = MD5Util.encode(dto.getCurrentPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        if (user.getPassword().equals(currentPassword))
            return ResponseEntity.badRequest().build();
        String newPassword = null;
        try {
            newPassword = MD5Util.encode(dto.getNewPassword());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
        user.setPassword(newPassword);
        user.setLastPasswordChangeTime(new Date());
        userService.save(user);
        return ResponseEntity.ok().build();
    }

}
