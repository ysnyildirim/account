package com.yil.account.group.repository;

import com.yil.account.group.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Group findByNameAndDeletedTimeIsNull(String name);

    boolean existsByNameAndDeletedTimeIsNull(String name);

    Page<Group> findAllByDeletedTimeIsNull(Pageable pageable);

    Group findByIdAndDeletedTimeIsNull(Long id);
}
