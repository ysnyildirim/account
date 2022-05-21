package com.yil.authentication.user.repository;

import com.yil.authentication.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findAllByUserIdAndDeletedTimeIsNull(Long userId);

    List<UserRole> findAllByUserIdAndRoleIdAndDeletedTimeIsNull(Long userId, Long roleId);

}
