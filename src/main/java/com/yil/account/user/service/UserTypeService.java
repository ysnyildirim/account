package com.yil.account.user.service;

import com.yil.account.exception.UserTypeNotFoundException;
import com.yil.account.user.dto.UserTypeDto;
import com.yil.account.user.model.UserType;
import com.yil.account.user.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    public static UserTypeDto toDto(UserType userType) throws NullPointerException {
        if (userType == null)
            throw new NullPointerException();
        return UserTypeDto.builder()
                .id(userType.getId())
                .name(userType.getName())
                .build();
    }

    public UserType save(UserType userType) {
        return userTypeRepository.save(userType);
    }

    public void deleteById(int id) {
        userTypeRepository.deleteById(id);
    }

    public UserType findById(int id) throws UserTypeNotFoundException {
        return userTypeRepository.findById(id).orElseThrow(() -> new UserTypeNotFoundException());
    }

    public boolean existsById(int id) {
        return userTypeRepository.existsById(id);
    }

    public List<UserType> findAll() {
        return userTypeRepository.findAll();
    }

}
