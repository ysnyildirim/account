package com.yil.account.role.repository;

import com.yil.account.role.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionDao extends JpaRepository<RolePermission, RolePermission.Pk> {
    List<RolePermission> findAllById_RoleId(Integer roleId);
}
