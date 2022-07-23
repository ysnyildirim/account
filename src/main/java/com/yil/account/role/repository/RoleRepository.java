package com.yil.account.role.repository;

import com.yil.account.role.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    boolean existsByName(String name);

}
