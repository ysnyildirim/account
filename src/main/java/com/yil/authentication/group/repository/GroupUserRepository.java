package com.yil.authentication.group.repository;

import com.yil.authentication.group.model.GroupUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    List<GroupUser> findAllByGroupIdAndDeletedTimeIsNull(Long groupId);

    List<GroupUser> findAllByGroupIdAndUserIdAndDeletedTimeIsNull(Long groupId, Long userId);
}
