package com.yil.account.user.service;

import com.yil.account.user.model.UserPhoto;
import com.yil.account.user.repository.UserPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPhotoService {
    private final UserPhotoRepository userPhotoRepository;

    public UserPhoto findById(Long id) throws EntityNotFoundException {
        return userPhotoRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
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
        return userPhotoRepository.findAllByUserIdAndDeletedTimeIsNull(pageable, userId);
    }

    public List<UserPhoto> findAllByUserIdAndPhotoIdAndDeletedTimeIsNull(Long userId, Long roleId) {
        return userPhotoRepository.findAllByUserIdAndPhotoIdAndDeletedTimeIsNull(userId, roleId);
    }

}
