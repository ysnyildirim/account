package com.yil.account.group.repository;

import com.yil.account.group.model.GroupRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, Long> {

    List<GroupRole> findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(Long groupId, Long roleId);

    Page<GroupRole> findAllByGroupIdAndDeletedTimeIsNull(Pageable pageable, Long groupId);
}
