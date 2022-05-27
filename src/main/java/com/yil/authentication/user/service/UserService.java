package com.yil.authentication.user.service;

import com.yil.authentication.exception.UserNotFoundException;
import com.yil.authentication.user.dto.UserDto;
import com.yil.authentication.user.model.User;
import com.yil.authentication.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
                .personId(user.getPersonId())
                .userTypeId(user.getUserTypeId())
                .build();
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException();
        });
    }

    public User findByIdAndDeletedTimeIsNull(Long id) throws UserNotFoundException {
        User user = userRepository.findByIdAndDeletedTimeIsNull(id);
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }

    public User findByUserNameAndDeletedTimeIsNull(String userName) throws UserNotFoundException {
        User user = userRepository.findByUserNameAndDeletedTimeIsNull(userName);
        if (user == null)
            throw new UserNotFoundException();
        return user;
    }

    public boolean existsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Page<User> findAllByDeletedTimeIsNull(Pageable pageable) {
        return userRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
