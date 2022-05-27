package com.yil.authentication.role.repository;

import com.yil.authentication.role.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findAllByRoleIdAndDeletedTimeIsNull(Long roleId);

    List<RolePermission> findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(Long roleId, Long permissionId);

}
