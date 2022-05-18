package com.yil.authentication.user.repository;

import com.yil.authentication.user.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {

    List<UserType> findAllByRealPersonAndDeletedTimeIsNull(Boolean realPerson);

    UserType findByNameAndDeletedTimeIsNull(String name);

    List<UserType> findAllByDeletedTimeIsNull();
}
