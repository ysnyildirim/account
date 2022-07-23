package com.yil.account.group.repository;

import com.yil.account.group.model.GroupRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, GroupRole.Pk> {

    Page<GroupRole> findAllById_GroupId(Pageable pageable, Long groupId);

    List<GroupRole> findAllById_GroupId(Long groupId);

}
