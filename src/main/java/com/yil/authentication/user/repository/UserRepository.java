package com.yil.authentication.user.repository;

import com.yil.authentication.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserNameAndDeletedTimeIsNull(String userName);

    Page<User> findAllByDeletedTimeIsNull(Pageable pageable);

    boolean existsByUserName(String userName);

    User findByIdAndDeletedTimeIsNull(Long id);

}
