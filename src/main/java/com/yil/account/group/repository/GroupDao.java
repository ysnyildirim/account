package com.yil.account.group.repository;

import com.yil.account.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupDao extends JpaRepository<Group, Long> {

    Group findByName(String name);

    boolean existsByName(String name);

}
