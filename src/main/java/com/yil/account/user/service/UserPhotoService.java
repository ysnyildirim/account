package com.yil.account.user.service;

import com.yil.account.exception.UserPhotoNotFound;
import com.yil.account.user.dao.UserPhotoDao;
import com.yil.account.user.dto.UserPhotoDto;
import com.yil.account.user.model.UserPhoto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPhotoService {
    private final UserPhotoDao userPhotoDao;

    public static UserPhotoDto toDto(UserPhoto userPhoto) {
        return UserPhotoDto.builder()
                .id(userPhoto.getId())
                .content(userPhoto.getContent())
                .extension(userPhoto.getExtension())
                .name(userPhoto.getName())
                .build();
    }

    public UserPhoto findById(Long id) throws UserPhotoNotFound {
        return userPhotoDao.findById(id).orElseThrow(() -> new UserPhotoNotFound());
    }

    public UserPhoto save(UserPhoto userPhoto) {
        return userPhotoDao.save(userPhoto);
    }

    public List<UserPhoto> saveAll(List<UserPhoto> roles) {
        return userPhotoDao.saveAll(roles);
    }

    public void deleteById(Long id) {
        userPhotoDao.deleteById(id);
    }

    public Page<UserPhoto> findAllByUserId(Pageable pageable, Long userId) {
        return userPhotoDao.findAllByUserId(pageable, userId);
    }

    public UserPhoto findByIdAndUserId(Long id, Long userId) throws UserPhotoNotFound {
        return userPhotoDao.findByIdAndUserId(id, userId).orElseThrow(UserPhotoNotFound::new);
    }
}
