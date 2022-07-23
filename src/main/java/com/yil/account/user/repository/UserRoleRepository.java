package com.yil.account.user.repository;

import com.yil.account.user.model.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.Pk> {

    Page<UserRole> findAllById_UserId(Pageable pageable, Long userId);

    List<UserRole> findAllById_UserId(Long userId);

}
