package com.yil.account.role.repository;

import com.yil.account.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleDao extends JpaRepository<Role, Integer> {

    boolean existsByName(String name);

}
