package com.yil.authentication.config;

import com.yil.authentication.base.MD5Util;
import com.yil.authentication.group.model.Group;
import com.yil.authentication.group.service.GroupRoleService;
import com.yil.authentication.group.service.GroupService;
import com.yil.authentication.group.service.GroupUserService;
import com.yil.authentication.role.model.Permission;
import com.yil.authentication.role.model.Role;
import com.yil.authentication.role.service.PermissionService;
import com.yil.authentication.role.service.RoleService;
import com.yil.authentication.user.model.User;
import com.yil.authentication.user.model.UserType;
import com.yil.authentication.user.service.UserService;
import com.yil.authentication.user.service.UserTypeService;
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

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");


        UserType adminUserType = null;
        try {
            adminUserType = userTypeService.findByName("ADMIN");
        } catch (Exception exception) {
        }
        if (adminUserType == null) {
            adminUserType = new UserType();
            adminUserType.setName("ADMIN");
            adminUserType.setRealPerson(true);
            userTypeService.save(adminUserType);
        }

        User adminUser = null;
        try {
            adminUser = userService.findByUserNameAndDeletedTimeIsNull("ADMIN");
        } catch (Exception exception) {
        }
        if (adminUser == null) {
            try {
                adminUser = User
                        .builder()
                        .userName("ADMIN")
                        .password(MD5Util.encode("admin"))
                        .enabled(true)
                        .locked(false)
                        .mail("admin@gmail.com")
                        .lastPasswordChangeTime(new Date())
                        .userTypeId(adminUserType.getId())
                        .build();
                userService.save(adminUser);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        permissions();
        roles();
        groups();
    }

    private void permissions() {
        addPermission("Group can create");
        addPermission("Group can update");
        addPermission("Group can select");
        addPermission("Group can delete");
        addPermission("Group role can create");
        addPermission("Group role can update");
        addPermission("Group role can select");
        addPermission("Group role can delete");
        addPermission("Group member can create");
        addPermission("Group member can update");
        addPermission("Group member can select");
        addPermission("Group member can delete");
        addPermission("User can create");
        addPermission("User can update");
        addPermission("User can select");
        addPermission("User can delete");
        addPermission("User type can create");
        addPermission("User type can update");
        addPermission("User type can select");
        addPermission("User type can delete");
        addPermission("User email can create");
        addPermission("User email can update");
        addPermission("User email can select");
        addPermission("User email can delete");
        addPermission("User address can create");
        addPermission("User address can update");
        addPermission("User address can select");
        addPermission("User address can delete");
        addPermission("User phone can create");
        addPermission("User phone can update");
        addPermission("User phone can select");
        addPermission("User phone can delete");
        addPermission("Permission can create");
        addPermission("Permission can update");
        addPermission("Permission can select");
        addPermission("Permission can delete");
        addPermission("Role can create");
        addPermission("Role can update");
        addPermission("Role can select");
        addPermission("Role can delete");
        addPermission("Role permission can create");
        addPermission("Role permission can update");
        addPermission("Role permission can select");
        addPermission("Role permission can delete");
    }

    private void roles() {
        addRole("GROUP_ADMIN", "Group admin");
        addRole("GROUP_STAFF", "Group staff");
        addRole("GROUP_USER", "Group user");
        addRole("GROUP_ROLE_ADMIN", "Group role admin");
        addRole("GROUP_ROLE_STAFF", "Group role staff");
        addRole("GROUP_ROLE_USER", "Group role user");
        addRole("GROUP_USER_ADMIN", "Group user admin");
        addRole("GROUP_USER_STAFF", "Group user staff");
        addRole("GROUP_USER_USER", "Group user user");
        addRole("ACCOUNT_ADMIN", "Account admin");
        addRole("ACCOUNT_STAFF", "Account staff");
        addRole("ACCOUNT_USER", "Account user");
        addRole("USER_TYPE_ADMIN", "User type admin");
        addRole("USER_TYPE_STAFF", "User type staff");
        addRole("USER_TYPE_USER", "User type user");
        addRole("USER_EMAIL_ADMIN", "User email admin");
        addRole("USER_EMAIL_STAFF", "User email staff");
        addRole("USER_EMAIL_USER", "User email user");
        addRole("USER_ADDRESS_ADMIN", "User address admin");
        addRole("USER_ADDRESS_STAFF", "User address staff");
        addRole("USER_ADDRESS_USER", "User address user");
        addRole("USER_PHONE_ADMIN", "User phone admin");
        addRole("USER_PHONE_STAFF", "User phone staff");
        addRole("USER_PHONE_USER", "User phone user");
        addRole("PERMISSION_ADMIN", "Permission admin");
        addRole("PERMISSION_STAFF", "Permission staff");
        addRole("PERMISSION_USER", "Permission user");
        addRole("ROLE_ADMIN", "Role admin");
        addRole("ROLE_STAFF", "Role staff");
        addRole("ROLE_USER", "Role user");
        addRole("ROLE_PERMISSION_ADMIN", "Role permission admin");
        addRole("ROLE_PERMISSION_STAFF", "Role permission staff");
        addRole("ROLE_PERMISSION_USER", "Role permission user");
    }

    private void groups() {
        addGroup("ADMIN");
        addGroup("STAFF");
        addGroup("USER");
    }

    private Role addRole(String name, String desc) {
        Role role = null;
        try {
            role = roleService.findByName(name);
        } catch (Exception exception) {
        }
        if (role == null) {
            role = new Role();
            role.setName(name);
            role.setDescription(desc);
            roleService.save(role);
        }
        return role;
    }

    private Group addGroup(String name) {
        Group entity = null;
        try {
            entity = groupService.findByName(name);
        } catch (Exception exception) {
        }
        if (entity == null) {
            entity = new Group();
            entity.setName(name);
            groupService.save(entity);
        }
        return entity;
    }

    private Permission addPermission(String name) {
        Permission entity = null;
        try {
            entity = permissionService.findByName(name);
        } catch (Exception exception) {
        }
        if (entity == null) {
            entity = new Permission();
            entity.setName(name);
            permissionService.save(entity);
        }
        return entity;
    }


}
