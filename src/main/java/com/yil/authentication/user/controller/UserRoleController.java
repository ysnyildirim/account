package com.yil.authentication.user.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.role.model.Role;
import com.yil.authentication.role.service.RoleService;
import com.yil.authentication.user.dto.CreateUserRoleDto;
import com.yil.authentication.user.dto.UserRoleDto;
import com.yil.authentication.exception.UserRoleNotFound;
import com.yil.authentication.user.model.UserRole;
import com.yil.authentication.user.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account/v1/users/{userId}/roles")
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final RoleService roleService;

    @Autowired
    public UserRoleController(UserRoleService userRoleService, RoleService roleService) {
        this.userRoleService = userRoleService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<UserRoleDto>> findAll(@PathVariable Long userId) {
        List<UserRole> data = userRoleService.findAllByUserIdAndDeletedTimeIsNull(userId);
        List<UserRoleDto> dtoData = new ArrayList<>();
        for (UserRole userRole : data) {
            UserRoleDto dto = UserRoleService.toDto(userRole);
            dtoData.add(dto);
        }
        return ResponseEntity.ok(dtoData);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long userId,
                                 @Valid @RequestBody CreateUserRoleDto dto) {
        Role role = roleService.findByIdAndDeletedTimeIsNull(dto.getRoleId());
        List<UserRole> userRoles = userRoleService.findAllByUserIdAndRoleIdAndDeletedTimeIsNull(userId, dto.getRoleId());
        if (!userRoles.isEmpty())
            return ResponseEntity.created(null).build();
        UserRole userRole = new UserRole();
        userRole.setUserId(userId);
        userRole.setRoleId(role.getId());
        userRole.setCreatedUserId(authenticatedUserId);
        userRole.setCreatedTime(new Date());
        userRole = userRoleService.save(userRole);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long userId,
                                 @PathVariable Long id) {
        List<UserRole> userRoles = userRoleService.findAllByUserIdAndRoleIdAndDeletedTimeIsNull(userId, id);
        if (userRoles.isEmpty())
            throw new UserRoleNotFound();
        for (UserRole userRole : userRoles) {
            userRole.setDeletedUserId(authenticatedUserId);
            userRole.setDeletedTime(new Date());
        }
        userRoleService.saveAll(userRoles);
        return ResponseEntity.ok().build();
    }

}
