package com.yil.authentication.group.repository;

import com.yil.authentication.group.model.GroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoleRepository extends JpaRepository<GroupRole, Long> {

    List<GroupRole> findAllByGroupIdAndDeletedTimeIsNull(Long groupId);

    List<GroupRole> findAllByGroupIdAndRoleIdAndDeletedTimeIsNull(Long groupId, Long roleId);

}
