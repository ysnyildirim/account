package com.yil.account.group.repository;

import com.yil.account.group.model.GroupUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, Long> {

    List<GroupUser> findAllByGroupIdAndUserIdAndDeletedTimeIsNull(Long groupId, Long userId);

    Page<GroupUser> findAllByGroupIdAndDeletedTimeIsNull(Pageable pageable, Long groupId);
}
