package com.yil.account.config;

import com.yil.account.base.MD5Util;
import com.yil.account.group.model.Group;
import com.yil.account.group.model.GroupRole;
import com.yil.account.group.model.GroupUserType;
import com.yil.account.group.repository.GroupDao;
import com.yil.account.group.repository.GroupUserTypeDao;
import com.yil.account.group.service.GroupRoleService;
import com.yil.account.role.model.Permission;
import com.yil.account.role.model.PermissionType;
import com.yil.account.role.model.Role;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.repository.PermissionDao;
import com.yil.account.role.repository.PermissionTypeDao;
import com.yil.account.role.service.RolePermissionService;
import com.yil.account.role.service.RoleService;
import com.yil.account.user.model.User;
import com.yil.account.user.model.UserType;
import com.yil.account.user.repository.UserTypeRepository;
import com.yil.account.user.service.UserService;
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

    public static UserType userTypeGercekKisi;
    public static UserType userTypeTuzelKisi;

    public static Group groupHerkes;
    public static Group groupGercekKisiler;
    public static Group groupTuzelKisiler;

    public static Role roleHerkes;
    public static Role roleGercek;
    public static Role roleTuzel;

    public static Permission permissionHerkes;
    public static Permission permissionGercek;
    public static Permission permissionTuzel;

    public static PermissionType permissionTypeSystem;
    public static PermissionType permissionTypeWorkflow;
    public static PermissionType permissionTypeAnother;

    public static User adminUser;

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private GroupDao groupDao;
    @Autowired
    private GroupRoleService groupRoleService;
    @Autowired
    private GroupUserTypeDao groupUserTypeDao;
    @Autowired
    private RolePermissionService rolePermissionService;


    @Autowired
    private PermissionTypeDao permissionTypeDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");

        try {
//            initUserTypes();
//            initGroupUserTypes();
//            initDefaultPermissionTypes();
//            initDefaultUsers();
//            initDefaultPermissions();
//            initDefaultRoles();
//            initDefaultGroups();
//
//
//            initSikayet();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initUserTypes() {
        userTypeGercekKisi = UserType.builder().id(1).name("Gerçek Kişi").build();
        userTypeRepository.save(userTypeGercekKisi);
        userTypeTuzelKisi = UserType.builder().id(2).name("Tüzel Kişi").build();
        userTypeRepository.save(userTypeTuzelKisi);
    }

    private void initGroupUserTypes() {
        groupUserTypeAdmin = GroupUserType.builder().id(1).name("ADMIN").description("Group admins").build();
        groupUserTypeDao.save(groupUserTypeAdmin);
        groupUserTypeManager = GroupUserType.builder().id(2).name("MANAGER").description("Group managers").build();
        groupUserTypeDao.save(groupUserTypeManager);
        groupUserTypeUser = GroupUserType.builder().id(3).name("USER").description("Group users").build();
        groupUserTypeDao.save(groupUserTypeUser);
    }

    private void initDefaultPermissionTypes() {
        permissionTypeSystem = PermissionType.builder().id(1).name("Sistem").build();
        permissionTypeDao.save(permissionTypeSystem);
        permissionTypeWorkflow = PermissionType.builder().id(2).name("İş Akışı").build();
        permissionTypeDao.save(permissionTypeWorkflow);
        permissionTypeAnother = PermissionType.builder().id(3).name("Diğer").build();
        permissionTypeDao.save(permissionTypeAnother);
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
                .userTypeId(userTypeGercekKisi.getId())
                .build();
        userService.save(adminUser);
    }

    private void initDefaultPermissions() {
        permissionHerkes = Permission.builder()
                .id(1l)
                .name("Herkes")
                .description("Herkes")
                .permissionTypeId(permissionTypeAnother.getId())
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        permissionDao.save(permissionHerkes);

        permissionGercek = Permission.builder()
                .id(2l)
                .name("Gerçek Kişi")
                .description("Gerçek kişi")
                .permissionTypeId(permissionTypeAnother.getId())
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        permissionDao.save(permissionGercek);

        permissionTuzel = Permission.builder()
                .id(3l)
                .name("Tüzel Kişi")
                .description("Tüzel kişi")
                .permissionTypeId(permissionTypeAnother.getId())
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        permissionDao.save(permissionTuzel);
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
        roleService.save(roleHerkes);
        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().roleId(roleHerkes.getId()).permissionId(permissionHerkes.getId()).build()).build());
        roleGercek = Role.builder()
                .id(2l)
                .name("Gerçek Kişi")
                .description("Gerçek kişilerin yetkileri")
                .assignable(true)
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        roleService.save(roleGercek);
        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().roleId(roleGercek.getId()).permissionId(permissionGercek.getId()).build()).build());
        roleTuzel = Role.builder()
                .id(3l)
                .name("Tüzel Kişi")
                .description("Tüzel kişilerin yetkileri")
                .assignable(true)
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build();
        roleService.save(roleTuzel);
        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().roleId(roleTuzel.getId()).permissionId(permissionTuzel.getId()).build()).build());
    }

    private void addRolePermission(RolePermission rolePermission) {
        if (rolePermissionService.existsById(rolePermission.getId()))
            return;
        rolePermissionService.save(rolePermission);
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
        groupDao.save(groupHerkes);

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
        groupDao.save(groupGercekKisiler);

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
        groupDao.save(groupTuzelKisiler);

        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().roleId(roleTuzel.getId()).groupId(groupTuzelKisiler.getId()).build()).build());
    }

    private void addGroupRole(GroupRole groupRole) {
        if (groupRoleService.existsById(groupRole.getId()))
            return;
        groupRoleService.save(groupRole);
    }

    private void initSikayet() {

        permissionDao.save(Permission.builder()
                .id(4l)
                .name("İşletmeler Tarafından Tüketici Şikayeti Cevaplama Yetkisi")
                .description("İşletmeler tarafından tüketici şikayetlerini cevaplama yetkisidir.")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build());

        permissionDao.save(Permission.builder()
                .id(5l)
                .name("Kurum Tarafından Tüketici Şikayeti Cevaplama Yetkisi")
                .description("Kurum kullanıcıları tarafından tüketici şikayetlerini cevaplama yetkisidir.")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .build());

        roleService.save(Role.builder()
                .id(4l)
                .name("Tüketici Şikayet İşletme")
                .description("Tüketici şikayet sistemindeki işletmeler")
                .assignable(true)
                .createdUserId(adminUser.getId())
                .createdTime(new Date())
                .build());

        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().permissionId(4l).roleId(4l).build()).build());

        roleService.save(Role.builder()
                .id(5l)
                .name("Tüketici Şikayet Kurum")
                .description("Tüketici şikayet sistemindeki kurum")
                .assignable(true)
                .createdUserId(adminUser.getId())
                .createdTime(new Date())
                .build());

        addRolePermission(RolePermission.builder().id(RolePermission.Pk.builder().permissionId(5l).roleId(5l).build()).build());

        groupDao.save(Group.builder().id(4L)
                .name("Tüketici Şikayet İşletme")
                .description("Tüketici şikayet sistemindeki işletmelerin bulunduğu grup")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .email("a@gmail.com").build());

        groupDao.save(Group.builder().id(5L)
                .name("Tüketici Şikayet Kurum")
                .description("Tüketici şikayet sistemindeki kurum kullanıcılarının bulunduğu grup")
                .createdTime(new Date())
                .createdUserId(adminUser.getId())
                .email("b@gmail.com").build());

        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().groupId(4l).roleId(4l).build()).build());
        addGroupRole(GroupRole.builder().id(GroupRole.Pk.builder().groupId(5l).roleId(5l).build()).build());
    }


}
