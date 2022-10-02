package com.yil.account.user.dao;

import com.yil.account.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User findByUserName(String userName);

    boolean existsByUserName(String userName);
}
