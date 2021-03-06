package com.yil.account.user.repository;

import com.yil.account.user.model.UserPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    Page<UserPhoto> findAllByUserId(Pageable pageable, Long userId);

    UserPhoto findByIdAndUserId(Long id, Long userId);

}
