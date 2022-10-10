package com.yil.account.user.controller;

import com.yil.account.base.Mapper;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.role.service.RoleService;
import com.yil.account.user.dto.UserRoleDto;
import com.yil.account.user.model.UserRole;
import com.yil.account.user.service.UserRoleService;
import com.yil.account.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/acc/v1/users/{userId}/roles")
public class UserRoleController {
    private final UserRoleService userRoleService;
    private final UserService userService;
    private final RoleService roleService;
    private final Mapper<UserRole, UserRoleDto> mapper = new Mapper<>(UserRoleService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserRoleDto>> findAll(@PathVariable Long userId) {
        return ResponseEntity.ok(mapper.map(userRoleService.findAllByUserId(userId)));
    }

    @PostMapping(value = "/{roleId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> create(@PathVariable Long userId,
                                         @PathVariable Integer roleId) throws RoleNotFoundException, UserNotFoundException {
        if (!roleService.existsById(roleId))
            throw new RoleNotFoundException();
        if (!userService.existsById(userId))
            throw new UserNotFoundException();
        UserRole.Pk pk = UserRole.Pk.builder().userId(userId).roleId(roleId).build();
        UserRole userRole = UserRole.builder().id(pk).build();
        userRoleService.save(userRole);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/{roleId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@PathVariable Long userId,
                                         @PathVariable Integer roleId) {
        userRoleService.delete(UserRole.Pk.builder().userId(userId).roleId(roleId).build());
        return ResponseEntity.ok().build();
    }
}
