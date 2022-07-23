package com.yil.account.config;

import com.yil.account.base.MD5Util;
import com.yil.account.group.model.GroupUserType;
import com.yil.account.group.repository.GroupUserTypeDao;
import com.yil.account.group.service.GroupRoleService;
import com.yil.account.group.service.GroupService;
import com.yil.account.group.service.GroupUserService;
import com.yil.account.group.service.GroupUserTypeService;
import com.yil.account.role.service.PermissionService;
import com.yil.account.role.service.RoleService;
import com.yil.account.user.model.User;
import com.yil.account.user.model.UserType;
import com.yil.account.user.service.UserService;
import com.yil.account.user.service.UserTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class SetupDataLoader implements ApplicationListener<ContextStartedEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRoleService groupRoleService;

    @Autowired
    private GroupUserService groupUserService;
    @Autowired
    private GroupUserTypeDao groupUserTypeDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");

        initUserTypes();
        initGroupUserTypes();
        try {
            initDefaultUsers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUserTypes() {
        addUserType(UserType.builder().id(UserTypeService.Admin).name("ADMIN").build());
        addUserType(UserType.builder().id(UserTypeService.User).name("USER").build());
        addUserType(UserType.builder().id(UserTypeService.Staff).name("STAFF").build());

    }

    private void initGroupUserTypes() {
        addGroupUserType(GroupUserType.builder().id(GroupUserTypeService.Admin).name("ADMIN").description("Group admins").build());
        addGroupUserType(GroupUserType.builder().id(GroupUserTypeService.Manager).name("MANAGER").description("Group managers").build());
        addGroupUserType(GroupUserType.builder().id(GroupUserTypeService.User).name("USER").description("Group users").build());
    }

    private void initDefaultUsers() throws NoSuchAlgorithmException {
        User adminUser = User
                .builder()
                .id(1L)
                .userName("ADMIN")
                .password(MD5Util.encode("admin"))
                .enabled(true)
                .locked(false)
                .mail("admin@gmail.com")
                .lastPasswordChangeTime(new Date())
                .userTypeId(UserTypeService.Admin)
                .build();
        addUser(adminUser);
    }

    private void addUserType(UserType type) {
        if (userTypeService.existsById(type.getId()))
            return;
        userTypeService.save(type);
    }

    private void addGroupUserType(GroupUserType type) {
        if (groupUserTypeDao.existsById(type.getId()))
            return;
        groupUserTypeDao.save(type);
    }

    private void addUser(User user) {
        if (userService.existsById(user.getId()))
            return;
        userService.save(user);
    }


}
