package com.yil.account.role.repository;

import com.yil.account.role.model.RoleAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleActionRepository extends JpaRepository<RoleAction, Long> {

    List<RoleAction> findAllByRoleIdAndActionIdAndDeletedTimeIsNull(Long roleId, Long permissionId);

    Page<RoleAction> findAllByRoleIdAndDeletedTimeIsNull(Pageable pageable, Long roleId);
}
