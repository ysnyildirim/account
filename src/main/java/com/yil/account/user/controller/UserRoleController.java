package com.yil.account.user.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.PageDto;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.exception.UserRoleNotFound;
import com.yil.account.role.dto.RoleDto;
import com.yil.account.role.model.Role;
import com.yil.account.role.service.RoleService;
import com.yil.account.user.dto.CreateUserRoleDto;
import com.yil.account.user.model.UserRole;
import com.yil.account.user.service.UserRoleService;
import com.yil.account.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/users/{userId}/roles")
public class UserRoleController {

    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageDto<RoleDto>> findAll(@PathVariable Long userId,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) throws RoleNotFoundException {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<UserRole> data = userRoleService.findAllById_UserId(pageable, userId);
        List<RoleDto> dtoData = new ArrayList<>();
        for (UserRole userRole : data.getContent()) {
            Role role = roleService.findById(userRole.getId().getRoleId());
            RoleDto dto = RoleService.convert(role);
            dtoData.add(dto);
        }
        PageDto<RoleDto> pageDto = new PageDto<>();
        pageDto.setTotalElements(data.getTotalElements());
        pageDto.setCurrentPage(data.getNumber());
        pageDto.setTotalPages(data.getTotalPages());
        pageDto.setContent(dtoData);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long userId,
                                 @Valid @RequestBody CreateUserRoleDto request) throws RoleNotFoundException, UserNotFoundException {
        if (!roleService.existsById(request.getRoleId()))
            throw new RoleNotFoundException();
        if (!userService.existsById(userId))
            throw new UserNotFoundException();
        UserRole.Pk id = UserRole.Pk.builder().userId(userId).roleId(request.getRoleId()).build();
        if (!userRoleService.existsById(id)) {
            UserRole userRole = new UserRole();
            userRole.setId(id);
            userRole = userRoleService.save(userRole);
        }
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long userId,
                                 @PathVariable Long id) throws UserRoleNotFound {
        UserRole.Pk pk = UserRole.Pk.builder().userId(userId).roleId(id).build();
        userRoleService.deleteById(pk);
        return ResponseEntity.ok().build();
    }

}
