package com.yil.account.config;

import com.yil.account.base.MD5Util;
import com.yil.account.group.model.Group;
import com.yil.account.group.model.GroupRole;
import com.yil.account.group.model.GroupUserType;
import com.yil.account.group.repository.GroupUserTypeDao;
import com.yil.account.group.service.GroupRoleService;
import com.yil.account.group.service.GroupService;
import com.yil.account.role.model.Permission;
import com.yil.account.role.model.Role;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.service.PermissionService;
import com.yil.account.role.service.RolePermissionService;
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

    public static GroupUserType groupUserTypeAdmin;
    public static GroupUserType groupUserTypeManager;
    public static GroupUserType groupUserTypeUser;

    public static UserType userTypeAdmin;
    public static UserType userTypeStandartUser;

    public static Group groupHerkes;
    public static Group groupGercekKisiler;
    public static Group groupTuzelKisiler;

    public static Role roleHerkes;
    public static Role roleGercek;
    public static Role roleTuzel;

    public static Permission permissionHerkes;
    public static Permission permissionGercek;
    public static Permission permissionTuzel;

    public static User adminUser;

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
    private GroupUserTypeDao groupUserTypeDao;
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");

        try {
            initUserTypes();
            initGroupUserTypes();
            initDefaultUsers();
            initDefaultPermissions();
            initDefaultRoles();
            initDefaultGroups();


            initSikayet();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initUserTypes() {
        userTypeAdmin = UserType.builder().id(1).name("Admin").build();
        addUserType(userTypeAdmin);
        userTypeStandartUser = UserType.builder().id(2).name("Standart User").build();
        addUserType(userTypeStandartUser);
    }

    private void initGroupUserTypes() {
        groupUserTypeAdmin = GroupUserType.builder().id(1).name("ADMIN").description("Group admins").build();
        addGroupUserType(groupUserTypeAdmin);
        groupUserTypeManager = GroupUserType.builder().id(2).name("MANAGER").description("Group managers").build();
        addGroupUserType(groupUserTypeManager);
        groupUserTypeUser = GroupUserType.builder().id(3).name("USER").description("Group users").build();
        addGroupUserType(groupUserTypeUser);
    }

    private void initDefaultUsers() throws NoSuchAlgorithmException {
        adminUser = User
                .builder()
                .id(1L)
                .userName("ADMIN")
                .password(MD5Util.encode("admin"))
                .enabled(true)
                .locked(false)
                .mail("admin@gmail.com")
                .lastPasswordChangeTime(new Date())
                .passwordNeedsChanged(false)
                .userTypeId(userTypeAdmin.getId())
                .build();
        addUser(adminUser);
    }

    private void initDefaultPermissions() {
        permissionHerkes = Permission.builder()
                .id(1l)
                .name("Herkes")
                .description("Herkes")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();

        addPermission(permissionHerkes);

        permissionGercek = Permission.builder()
                .id(2l)
                .name("Gerçek Kişi")
                .description("Gerçek kişi")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        addPermission(permissionGercek);

        permissionTuzel = Permission.builder()
                .id(3l)
                .name("Tüzel Kişi")
                .description("Tüzel kişi")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        addPermission(permissionTuzel);


    }

    private void initDefaultRoles() {
        roleHerkes = Role.builder()
                .id(1l)
                .name("Herkes")
                .description("Herkes")
                .assignable(true)
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        addRole(roleHerkes);
        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().roleId(roleHerkes.getId()).permissionId(permissionHerkes.getId()).build()).build());
        roleGercek = Role.builder()
                .id(2l)
                .name("Gerçek Kişi")
                .description("Gerçek kişilerin yetkileri")
                .assignable(true)
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        addRole(roleGercek);
        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().roleId(roleGercek.getId()).permissionId(permissionGercek.getId()).build()).build());
        roleTuzel = Role.builder()
                .id(3l)
                .name("Tüzel Kişi")
                .description("Tüzel kişilerin yetkileri")
                .assignable(true)
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        addRole(roleTuzel);
        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().roleId(roleTuzel.getId()).permissionId(permissionTuzel.getId()).build()).build());
    }

    private void initDefaultGroups() {
        groupHerkes = Group
                .builder()
                .name("Herkes")
                .description("Tüm kullanıcılar")
                .email("heskes@gmail.com")
                .id(1L)
                .createdUserId(adminUser.getId())
                .createdTime(new Date())
                .build();
        addGroup(groupHerkes);

        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().roleId(roleHerkes.getId()).groupId(groupHerkes.getId()).build()).build());

        groupGercekKisiler = Group
                .builder()
                .name("Gerçek Kişiler")
                .description("Tüm gerçek kişiler")
                .email("gercekkisiler@gmail.com")
                .id(2L)
                .createdUserId(adminUser.getId())
                .createdTime(new Date())
                .build();
        addGroup(groupGercekKisiler);

        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().roleId(roleGercek.getId()).groupId(groupGercekKisiler.getId()).build()).build());

        groupTuzelKisiler = Group
                .builder()
                .name("Tüzel Kişiler")
                .description("Tüm tüzel kişiler")
                .email("tuzelkisiler@gmail.com")
                .id(3L)
                .createdUserId(adminUser.getId())
                .createdTime(new Date())
                .build();
        addGroup(groupTuzelKisiler);

        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().roleId(roleTuzel.getId()).groupId(groupTuzelKisiler.getId()).build()).build());
    }

    private void initSikayet() {

        addPermission(Permission.builder()
                .id(4l)
                .name("İşletmeler Tarafından Tüketici Şikayeti Cevaplama Yetkisi")
                .description("İşletmeler tarafından tüketici şikayetlerini cevaplama yetkisidir.")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build());

        addPermission(Permission.builder()
                .id(5l)
                .name("Kurum Tarafından Tüketici Şikayeti Cevaplama Yetkisi")
                .description("Kurum kullanıcıları tarafından tüketici şikayetlerini cevaplama yetkisidir.")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build());


        addRole(Role.builder()
                .id(4l)
                .name("Tüketici Şikayet İşletme")
                .description("Tüketici şikayet sistemindeki işletmeler")
                .assignable(true)
                .createdUserId(adminUser.getId())
                .createdTime(new Date())
                .build());

        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().permissionId(4l).roleId(4l).build()).build());

        addRole(Role.builder()
                .id(5l)
                .name("Tüketici Şikayet Kurum")
                .description("Tüketici şikayet sistemindeki kurum")
                .assignable(true)
                .createdUserId(adminUser.getId())
                .createdTime(new Date())
                .build());

        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().permissionId(5l).roleId(5l).build()).build());

        addGroup(Group.builder().id(4L)
                .name("Tüketici Şikayet İşletme")
                .description("Tüketici şikayet sistemindeki işletmelerin bulunduğu grup")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .email("a@gmail.com").build());

        addGroup(Group.builder().id(5L)
                .name("Tüketici Şikayet Kurum")
                .description("Tüketici şikayet sistemindeki kurum kullanıcılarının bulunduğu grup")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .email("b@gmail.com").build());

        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().groupId(4l).roleId(4l).build()).build());
        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().groupId(5l).roleId(5l).build()).build());
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

    private void addPermission(Permission permission) {
        if (permissionService.existsById(permission.getId()))
            return;
        permissionService.save(permission);
    }

    private void addRole(Role role) {
        if (roleService.existsById(role.getId()))
            return;
        roleService.save(role);
    }

    private void addRolePermission(RolePermission rolePermission) {
        if (rolePermissionService.existsById(rolePermission.getId()))
            return;
        rolePermissionService.save(rolePermission);
    }

    private void addGroup(Group group) {
        if (groupService.existsById(group.getId()))
            return;
        groupService.save(group);
    }

    private void addGroupRole(GroupRole groupRole) {
        if (groupRoleService.existsById(groupRole.getId()))
            return;
        groupRoleService.save(groupRole);
    }


}
