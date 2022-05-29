package com.yil.account.role.repository;

import com.yil.account.role.model.Action;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ActionRepository extends JpaRepository<Action, Long> {

    Action findByIdAndDeletedTimeIsNull(Long id);

    Page<Action> findAllByDeletedTimeIsNull(Pageable pageable);
}
