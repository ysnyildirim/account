package com.yil.account.user.service;

import com.yil.account.exception.UserNotFoundException;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.service.RolePermissionService;
import com.yil.account.role.service.RoleService;
import com.yil.account.user.dao.UserDao;
import com.yil.account.user.dto.UserDto;
import com.yil.account.user.model.User;
import com.yil.account.user.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDao userDao;
    private final RolePermissionService rolePermissionService;
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .enabled(user.isEnabled())
                .locked(user.isLocked())
                .mail(user.getMail())
                .personId(user.getPersonId())
                .userTypeId(user.getUserTypeId())
                .lastPasswordChangeDate(user.getLastPasswordChangeDate())
                .passwordNeedsChanged(user.isPasswordNeedsChanged())
                .build();
    }

    public User findById(Long id) throws UserNotFoundException {
        return userDao.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    public boolean existsById(Long id) {
        return userDao.existsById(id);
    }

    public User findByUserName(String userName) throws UserNotFoundException {
        User user = userDao.findByUserName(userName);
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }

    public void deleteById(long id) {
        userDao.deleteById(id);
    }

    public boolean existsByUserName(String userName) {
        return userDao.existsByUserName(userName);
    }

    public User save(User user) {
        return userDao.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userDao.findAll(pageable);
    }

    public boolean existsByPermission(Long userId, Integer permissionId) {
        List<UserRole> roles = userRoleService.findAllByUserId(userId);
        for (UserRole role : roles) {
            if (rolePermissionService.existsById(RolePermission.Pk.builder()
                    .roleId(role.getId().getRoleId())
                    .permissionId(permissionId).build()))
                return true;
        }
        return false;
    }
}
