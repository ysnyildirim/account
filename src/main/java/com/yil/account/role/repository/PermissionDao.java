package com.yil.account.role.repository;

import com.yil.account.role.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao extends JpaRepository<Permission, Integer> {
    boolean existsByName(String name);
}
