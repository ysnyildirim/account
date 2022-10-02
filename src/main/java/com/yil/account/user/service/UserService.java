package com.yil.account.user.service;

import com.yil.account.exception.UserNotFoundException;
import com.yil.account.group.service.GroupRoleService;
import com.yil.account.group.service.GroupService;
import com.yil.account.group.service.GroupUserService;
import com.yil.account.role.service.RolePermissionService;
import com.yil.account.role.service.RoleService;
import com.yil.account.user.dto.UserDto;
import com.yil.account.user.model.User;
import com.yil.account.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RolePermissionService rolePermissionService;
    private final GroupUserService groupUserService;
    private final GroupRoleService groupRoleService;
    private final RoleService roleService;
    private final GroupService groupService;

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .enabled(user.isEnabled())
                .locked(user.isLocked())
                .mail(user.getMail())
                .personId(user.getPersonId())
                .userTypeId(user.getUserTypeId())
                .build();
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
    }

    public boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public User findByUserName(String userName) throws UserNotFoundException {
        User user = userRepository.findByUserName(userName);
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public boolean existsByPermission(Long id, Long permissionId) {
        List<Long> groupUsers = groupUserService.getGroupIdByUserId(id);
        for (Long groupUser : groupUsers) {
            if (groupService.existsGroupPermission(groupUser, permissionId))
                return true;
        }
        return false;
    }


}
