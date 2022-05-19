package com.yil.authentication.user.service;

import com.yil.authentication.user.controller.dto.UserDto;
import com.yil.authentication.user.model.User;
import com.yil.authentication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .enabled(user.getEnabled())
                .locked(user.getLocked())
                .mail(user.getMail())
                .userTypeId(user.getUserTypeId())
                .build();
    }

    public User findById(Long id) throws EntityNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException();
        });
    }

    public User findByUserNameAndDeletedTimeIsNull(String userName) throws EntityNotFoundException {
        User user = userRepository.findByUserNameAndDeletedTimeIsNull(userName);
        if (user == null)
            throw new EntityNotFoundException();
        return user;
    }

    public boolean existsByUserNameAndDeletedTimeIsNull(String userName) {
        return userRepository.existsByUserNameAndDeletedTimeIsNull(userName);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Page<User> findAllByDeletedTimeIsNull(Pageable pageable) {
        return userRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
