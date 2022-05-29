package com.yil.account.role.repository;

import com.yil.account.role.model.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {

    List<RolePermission> findAllByRoleIdAndPermissionIdAndDeletedTimeIsNull(Long roleId, Long permissionId);

    Page<RolePermission> findAllByRoleIdAndDeletedTimeIsNull(Pageable pageable, Long roleId);
}
