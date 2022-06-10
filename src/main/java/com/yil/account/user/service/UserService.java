package com.yil.account.user.service;

import com.yil.account.exception.DisabledUserException;
import com.yil.account.exception.LockedUserException;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.user.dto.UserDto;
import com.yil.account.user.model.User;
import com.yil.account.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

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
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
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

    public boolean isActive(User user) throws DisabledUserException, LockedUserException {
        if (!user.getEnabled())
            throw new DisabledUserException();
        if (user.getLocked())
            throw new LockedUserException();
        return true;
    }

    public User getActiveUser(String userName) throws UserNotFoundException, LockedUserException, DisabledUserException {
        User user = findByUserNameAndDeletedTimeIsNull(userName);
        isActive(user);
        return user;
    }

    public User getActiveUser(Long id) throws UserNotFoundException, LockedUserException, DisabledUserException {
        User user = findByIdAndDeletedTimeIsNull(id);
        isActive(user);
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
