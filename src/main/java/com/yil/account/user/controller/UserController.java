package com.yil.account.user.controller;

import com.yil.account.auth.JwtTokenUtil;
import com.yil.account.base.ApiConstant;
import com.yil.account.base.MD5Util;
import com.yil.account.base.PageDto;
import com.yil.account.exception.*;
import com.yil.account.role.service.PermissionService;
import com.yil.account.user.dto.CreateUserDto;
import com.yil.account.user.dto.CreateUserResponse;
import com.yil.account.user.dto.UserDto;
import com.yil.account.user.dto.UserPasswordDto;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import com.yil.account.user.service.UserTypeService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/users")
public class UserController {

    private final UserService userService;
    private final UserTypeService userTypeService;
    private final JwtTokenUtil jwtTokenUtil;
    @Autowired
    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<PageDto<UserDto>> findAll(
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
            @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.findAll(pageable);
        PageDto<UserDto> pageDto = PageDto.toDto(userPage, UserService::convert);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        UserDto dto = UserService.convert(user);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "/userName={userName}")
    public ResponseEntity<UserDto> findByUserName(@PathVariable String userName) throws UserNotFoundException {
        User user = userService.findByUserName(userName);
        UserDto dto = UserService.convert(user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CreateUserResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                     @Valid @RequestBody CreateUserDto request) throws UserNameCannotBeUsedException, UserTypeNotFoundException, NoSuchAlgorithmException {
        if (userService.existsByUserName(request.getUserName()))
            throw new UserNameCannotBeUsedException();
        if (!userTypeService.existsById(request.getUserTypeId()))
            throw new UserTypeNotFoundException();
        String hashPassword = MD5Util.encode(request.getPassword());
        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(hashPassword);
        user.setUserTypeId(request.getUserTypeId());
        user.setEnabled(request.getEnabled());
        user.setLocked(request.getLocked());
        user.setMail(request.getMail());
        user.setPasswordNeedsChanged(request.getPasswordNeedsChanged());
        user.setLastPasswordChangeTime(new Date());
        user.setPersonId(request.getPersonId());
        user.setCreatedUserId(authenticatedUserId);
        user.setCreatedTime(new Date());
        user = userService.save(user);
        CreateUserResponse createUserResponse = CreateUserResponse.builder().id(user.getId()).build();
        return ResponseEntity.created(null).body(createUserResponse);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @PathVariable Long id) throws UserNotFoundException {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/lock")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity lock(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                               @PathVariable Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        user.setLocked(true);
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

    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity active(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        user.setEnabled(true);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/inactive")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity inactive(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                   @PathVariable Long id) throws UserNotFoundException {
        User user = userService.findById(id);
        user.setEnabled(false);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/change-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity changePassword(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id,
                                         @RequestBody UserPasswordDto request) throws UserNotFoundException, NoSuchAlgorithmException, LockedUserException, DisabledUserException, WrongPasswordException {
        User user = userService.findById(id);
        if (!user.isEnabled())
            throw new DisabledUserException();
        if (user.isLocked())
            throw new LockedUserException();
        String currentPassword = MD5Util.encode(request.getCurrentPassword());
        if (!user.getPassword().equals(currentPassword))
            throw new WrongPasswordException();
        String newPassword = MD5Util.encode(request.getNewPassword());
        user.setPassword(newPassword);
        user.setLastPasswordChangeTime(new Date());
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/permission-id={permissionId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity existsPermission(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                           @PathVariable Long id,
                                           @PathVariable Long permissionId) {
        if (userService.existsByPermission(id, permissionId))
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.noContent().build();
    }

}
