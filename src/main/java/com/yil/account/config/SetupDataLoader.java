package com.yil.account.config;

import com.yil.account.base.MD5Util;
import com.yil.account.role.model.Permission;
import com.yil.account.role.model.Role;
import com.yil.account.role.model.RolePermission;
import com.yil.account.role.repository.PermissionDao;
import com.yil.account.role.repository.RoleDao;
import com.yil.account.role.repository.RolePermissionDao;
import com.yil.account.user.dao.UserDao;
import com.yil.account.user.dao.UserRoleDao;
import com.yil.account.user.dao.UserTypeDao;
import com.yil.account.user.model.User;
import com.yil.account.user.model.UserRole;
import com.yil.account.user.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class SetupDataLoader implements ApplicationListener<ContextStartedEvent> {
    public static UserType userTypeGercekKisi;
    public static UserType userTypeTuzelKisi;
    public static Role roleAdmin;
    public static Role roleHerkes;
    public static Role roleGercek;
    public static Role roleTuzel;
    public static User adminUser;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserTypeDao userTypeDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        System.out.println("Start Up Events");
        System.out.println(new Date(event.getTimestamp()));
        System.out.println("----------------------");
        try {
            initUserTypes();
            initDefaultUsers();
            initDefaultRoles();

            // initSikayet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUserTypes() {
        userTypeGercekKisi = UserType.builder().id(1).name("Gerçek Kişi").build();
        userTypeDao.save(userTypeGercekKisi);
        userTypeTuzelKisi = UserType.builder().id(2).name("Tüzel Kişi").build();
        userTypeDao.save(userTypeTuzelKisi);
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
                .lastPasswordChangeDate(new Date())
                .passwordNeedsChanged(false)
                .userTypeId(userTypeGercekKisi.getId())
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
