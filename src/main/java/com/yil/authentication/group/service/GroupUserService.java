package com.yil.authentication.group.service;

import com.yil.authentication.group.controller.dto.GroupUserDto;
import com.yil.authentication.group.model.GroupUser;
import com.yil.authentication.group.repository.GroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class GroupUserService {
    private final GroupUserRepository groupUserRepository;

    @Autowired
    public GroupUserService(GroupUserRepository groupUserRepository) {
        this.groupUserRepository = groupUserRepository;
    }

    public static GroupUserDto toDto(GroupUser f) throws NullPointerException {
        if (f == null)
            throw new NullPointerException();
        GroupUserDto dto = new GroupUserDto();
        dto.setUserId(f.getUserId());
        dto.setGroupId(f.getGroupId());
        return dto;
    }

    public GroupUser findById(Long id) throws EntityNotFoundException {
        return groupUserRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException();
        });
    }

    public GroupUser save(GroupUser groupUser) {
        return groupUserRepository.save(groupUser);
    }

    public List<GroupUser> saveAll(List<GroupUser> roles) {
        return groupUserRepository.saveAll(roles);
    }

    public void delete(Long id) {
        groupUserRepository.deleteById(id);
    }

    public List<GroupUser> findAllByGroupIdAndDeletedTimeIsNull(Long groupId) {
        return groupUserRepository.findAllByGroupIdAndDeletedTimeIsNull(groupId);
    }

    public List<GroupUser> findAllByGroupIdAndUserIdAndDeletedTimeIsNull(Long groupId, Long userId) {
        return groupUserRepository.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(groupId, userId);
    }

}
