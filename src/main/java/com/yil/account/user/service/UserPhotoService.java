package com.yil.account.user.service;

import com.yil.account.exception.UserPhotoNotFound;
import com.yil.account.user.dto.UserPhotoDto;
import com.yil.account.user.model.UserPhoto;
import com.yil.account.user.repository.UserPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPhotoService {
    private final UserPhotoRepository userPhotoRepository;

    public static UserPhotoDto toDto(UserPhoto userPhoto) {
        return UserPhotoDto.builder()
                .id(userPhoto.getId())
                .content(userPhoto.getContent())
                .extension(userPhoto.getExtension())
                .name(userPhoto.getName())
                .build();
    }

    public UserPhoto findById(Long id) throws UserPhotoNotFound {
        return userPhotoRepository.findById(id).orElseThrow(() -> new UserPhotoNotFound());
    }

    public UserPhoto save(UserPhoto userPhoto) {
        return userPhotoRepository.save(userPhoto);
    }

    public List<UserPhoto> saveAll(List<UserPhoto> roles) {
        return userPhotoRepository.saveAll(roles);
    }

    public void delete(Long id) {
        userPhotoRepository.deleteById(id);
    }

    public Page<UserPhoto> findAllByUserIdAndDeletedTimeIsNull(Pageable pageable, Long userId) {
        return userPhotoRepository.findAllByUserId(pageable, userId);
    }

    public UserPhoto findByIdAndUserIdAndDeletedTimeIsNull(Long id, Long userId) throws UserPhotoNotFound {
        UserPhoto userPhoto = userPhotoRepository.findByIdAndUserId(id, userId);
        if (userPhoto == null)
            throw new UserPhotoNotFound();
        return userPhoto;
    }
}
