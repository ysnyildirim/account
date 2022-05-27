package com.yil.authentication.role.repository;

import com.yil.authentication.role.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findByNameAndDeletedTimeIsNull(String name);

    boolean existsByNameAndDeletedTimeIsNull(String name);

    Permission findByIdAndDeletedTimeIsNull(Long id);

    Page<Permission> findAllByDeletedTimeIsNull(Pageable pageable);
}
