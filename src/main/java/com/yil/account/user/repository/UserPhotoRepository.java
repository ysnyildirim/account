package com.yil.account.user.repository;

import com.yil.account.user.model.UserPhoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

    Page<UserPhoto> findAllByUserIdAndDeletedTimeIsNull(Pageable pageable, Long userId);

    List<UserPhoto> findAllByUserIdAndPhotoIdAndDeletedTimeIsNull(Long userId, Long roleId);

}
