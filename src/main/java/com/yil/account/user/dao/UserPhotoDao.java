package com.yil.account.user.dao;

import com.yil.account.user.model.UserPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPhotoDao extends JpaRepository<UserPhoto, Long> {

    Page<UserPhoto> findAllByUserId(Pageable pageable, Long userId);

    Optional<UserPhoto> findByIdAndUserId(Long id, Long userId);

}
