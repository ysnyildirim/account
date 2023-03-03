package com.yil.account.user.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.MD5Util;
import com.yil.account.base.Mapper;
import com.yil.account.base.PageDto;
import com.yil.account.exception.LockedUserException;
import com.yil.account.exception.UserNameCannotBeUsedException;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.exception.WrongPasswordException;
import com.yil.account.role.service.PermissionService;
import com.yil.account.user.dto.UserChangePasswordRequest;
import com.yil.account.user.dto.UserDto;
import com.yil.account.user.dto.UserRequest;
import com.yil.account.user.dto.UserResponse;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/acc/v1/users")
public class UserController {
    private final UserService userService;
    private final PermissionService permissionService;
    private final Mapper<User, UserDto> mapper = new Mapper<>(UserService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDto<UserDto>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(mapper.map(userService.findAll(pageable)));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> findById(@PathVariable Long id) throws UserNotFoundException {
        return ResponseEntity.ok(mapper.map(userService.findById(id)));
    }

    @GetMapping(value = "/userName={userName}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDto> findByUserName(@PathVariable String userName) throws UserNotFoundException {
        return ResponseEntity.ok(mapper.map(userService.findByUserName(userName)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID, required = false) Long authenticatedUserId,
                                               @Valid @RequestBody UserRequest request) throws UserNameCannotBeUsedException, NoSuchAlgorithmException {
        if (userService.existsByUserName(request.getUserName()))
            throw new UserNameCannotBeUsedException();
        String hashPassword = MD5Util.encode(request.getPassword());
        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(hashPassword);
        user.setLocked(request.getLocked());
        user.setMail(request.getMail());
        user.setExpiredPassword(request.getExpiredPassword());
        user.setLastPasswordChange(new Date());
        user.setCreatedUserId(authenticatedUserId);
        user.setCreatedDate(new Date());
        user = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.builder().id(user.getId()).build());
    }

    @PutMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity lock(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                               @PathVariable Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        user.setLocked(true);
        user.setLastModifyUserId(authenticatedUserId);
        user.setLastModifyDate(new Date());
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/unlock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity unlock(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        user.setLocked(false);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/change-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity changePassword(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id,
                                         @RequestBody UserChangePasswordRequest request) throws UserNotFoundException, NoSuchAlgorithmException, LockedUserException, WrongPasswordException {
        User user = userService.findById(id);
        if (user.isLocked())
            throw new LockedUserException();
        String currentPassword = MD5Util.encode(request.getCurrentPassword());
        if (!user.getPassword().equals(currentPassword))
            throw new WrongPasswordException();
        String newPassword = MD5Util.encode(request.getNewPassword());
        user.setPassword(newPassword);
        user.setLastPasswordChange(new Date());
        user.setLastModifyDate(new Date());
        user.setLastModifyUserId(authenticatedUserId);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/permission-id={permissionId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity existsPermission(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                           @PathVariable Long id,
                                           @PathVariable Integer permissionId) {
        if (userService.existsByPermission(id, permissionId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.noContent().build();
    }
}
