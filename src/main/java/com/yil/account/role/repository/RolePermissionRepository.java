package com.yil.account.role.repository;

import com.yil.account.role.model.RolePermission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermission.Pk> {

    Page<RolePermission> findAllById_RoleId(Pageable pageable, Long roleId);

}
