package com.yil.authentication.role.repository;

import com.yil.authentication.role.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByNameAndDeletedTimeIsNull(String name);

    boolean existsByNameAndDeletedTimeIsNull(String name);

    Page<Role> findAllByDeletedTimeIsNull(Pageable pageable);

    Role findByIdAndDeletedTimeIsNull(Long id);
}
