package com.yil.account.user.service;

import com.yil.account.exception.UserTypeNotFoundException;
import com.yil.account.user.dto.UserTypeDto;
import com.yil.account.user.model.UserType;
import com.yil.account.user.repository.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTypeService {

    private final UserTypeRepository userTypeRepository;

    @Autowired
    public UserTypeService(UserTypeRepository userTypeRepository) {
        this.userTypeRepository = userTypeRepository;
    }

    public static UserTypeDto toDto(UserType userType) throws NullPointerException {
        if (userType == null)
            throw new NullPointerException();
        return UserTypeDto.builder()
                .id(userType.getId())
                .name(userType.getName())
                .realPerson(userType.getRealPerson())
                .build();
    }

    public UserType save(UserType userType) {
        return userTypeRepository.save(userType);
    }

    public void delete(Long id) {
        userTypeRepository.deleteById(id);
    }

    public UserType findById(Long id) throws UserTypeNotFoundException {
        return userTypeRepository.findById(id).orElseThrow(() -> new UserTypeNotFoundException());
    }

    public List<UserType> findAllByRealPersonAndDeletedTimeIsNull(Boolean realPerson) {
        return userTypeRepository.findAllByRealPersonAndDeletedTimeIsNull(realPerson);
    }

    public UserType findByName(String name) throws UserTypeNotFoundException {
        UserType userType = userTypeRepository.findByNameAndDeletedTimeIsNull(name);
        if (userType == null) throw new UserTypeNotFoundException();
        return userType;
    }

    public List<UserType> findAllByDeletedTimeIsNull() {
        return userTypeRepository.findAllByDeletedTimeIsNull();
    }

    public UserType findByIdAndDeletedTimeIsNull(Long id) throws UserTypeNotFoundException {
        UserType userType = userTypeRepository.findByIdAndDeletedTimeIsNull(id);
        if (userType == null)
            throw new UserTypeNotFoundException();
        return userType;
    }
}
