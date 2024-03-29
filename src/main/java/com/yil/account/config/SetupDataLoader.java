package com.yil.account.config;

import com.yil.account.base.MD5Util;
import com.yil.account.role.model.Permission;
import com.yil.account.role.model.PermissionType;
import com.yil.account.role.model.Role;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.repository.PermissionDao;
import com.yil.account.role.repository.PermissionTypeDao;
import com.yil.account.role.repository.RoleDao;
import com.yil.account.role.repository.RolePermissionDao;
import com.yil.account.user.dao.UserDao;
import com.yil.account.user.dao.UserRoleDao;
import com.yil.account.user.model.User;
import com.yil.account.user.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class SetupDataLoader implements ApplicationListener<ContextStartedEvent> {

    public static Role roleAdmin;
    public static Role roleHerkes;
    public static Role roleGercek;
    public static Role roleTuzel;
    public static User adminUser;
    public static PermissionType permissionTypeEndPoint;
    public static PermissionType permissionTypeAkis;

    @Autowired
    private UserDao userDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private PermissionTypeDao permissionTypeDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        try {
            initDefaultUsers();
            initDefaultRoles();
            initPermissionTypes();
            // initSikayet();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initDefaultUsers() throws NoSuchAlgorithmException {
        adminUser = User
                .builder()
                .id(1L)
                .userName("ADMIN")
                .password(MD5Util.encode("admin"))
                .locked(false)
                .mail("admin@gmail.com")
                .lastPasswordChange(new Date())
                .expiredPassword(false)
                .build();
        userDao.save(adminUser);
        userRoleDao.save(UserRole.builder().id(UserRole.Pk.builder().roleId(1).userId(adminUser.getId()).build()).build());
    }

    private void initDefaultRoles() {
        roleAdmin = Role.builder()
                .id(1)
                .name("ADMIN")
                .description("Sİstem admini")
                .assignable(false)
                .createdDate(new Date())
                .createdUserId(adminUser.getId())
                .build();
        roleDao.save(roleAdmin);
        roleHerkes = Role.builder()
                .id(2)
                .name("HERKES")
                .description("Sistemdeki tüm kullanıcılar")
                .assignable(true)
                .createdDate(new Date())
                .createdUserId(adminUser.getId())
                .build();
        roleDao.save(roleHerkes);
        roleGercek = Role.builder()
                .id(3)
                .name("GERCEK_KULLANICI")
                .description("Gerçek kişilerin yetkileri")
                .assignable(true)
                .createdDate(new Date())
                .createdUserId(adminUser.getId())
                .build();
        roleDao.save(roleGercek);
        roleTuzel = Role.builder()
                .id(4)
                .name("TUZEL_KULLANICI")
                .description("Tüzel kişilerin yetkileri")
                .assignable(true)
                .createdDate(new Date())
                .createdUserId(adminUser.getId())
                .build();
        roleDao.save(roleTuzel);
    }

    private void initPermissionTypes() {
        permissionTypeEndPoint = permissionTypeDao.save(PermissionType.builder()
                .id(1)
                .name("End Point")
                .build());
        permissionTypeAkis = permissionTypeDao.save(PermissionType.builder()
                .id(2)
                .name("Akış")
                .build());
    }

    private void initSikayet() {
        permissionDao.save(Permission.builder()
                .id(4)
                .name("İşletmeler Tarafından Tüketici Şikayeti Cevaplama Yetkisi")
                .description("İşletmeler tarafından tüketici şikayetlerini cevaplama yetkisidir.")
                .createdDate(new Date())
                .createdUserId(adminUser.getId())
                .build());
        permissionDao.save(Permission.builder()
                .id(5)
                .name("Kurum Tarafından Tüketici Şikayeti Cevaplama Yetkisi")
                .description("Kurum kullanıcıları tarafından tüketici şikayetlerini cevaplama yetkisidir.")
                .createdDate(new Date())
                .createdUserId(adminUser.getId())
                .build());
        roleDao.save(Role.builder()
                .id(4)
                .name("Tüketici Şikayet İşletme")
                .description("Tüketici şikayet sistemindeki işletmeler")
                .assignable(true)
                .createdUserId(adminUser.getId())
                .createdDate(new Date())
                .build());
        rolePermissionDao.save(RolePermission.builder().id(RolePermission.Pk.builder().permissionId(4).roleId(4).build()).build());
        roleDao.save(Role.builder()
                .id(5)
                .name("Tüketici Şikayet Kurum")
                .description("Tüketici şikayet sistemindeki kurum")
                .assignable(true)
                .createdUserId(adminUser.getId())
                .createdDate(new Date())
                .build());
        rolePermissionDao.save(RolePermission.builder().id(RolePermission.Pk.builder().permissionId(5).roleId(5).build()).build());
    }
}
