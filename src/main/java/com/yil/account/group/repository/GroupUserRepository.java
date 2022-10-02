package com.yil.account.group.repository;

import com.yil.account.group.model.GroupUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupUserRepository extends JpaRepository<GroupUser, GroupUser.Pk> {

    Page<GroupUser> findAllById_GroupId(Pageable pageable, Long groupId);

    List<GroupUser> findAllById_UserId(Long userId);

    @Query(nativeQuery = true,
            value = "select group_id from grp.group_user where user_id=:userId ")
    List<Long> getGroupIdByUserId(@Param(value = "userId") long userId);

}
