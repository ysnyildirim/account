package com.yil.account.group.service;

import com.yil.account.group.dto.GroupUserDto;
import com.yil.account.group.model.GroupUser;
import com.yil.account.group.repository.GroupUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return groupUserRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
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

    public List<GroupUser> findAllByGroupIdAndUserIdAndDeletedTimeIsNull(Long groupId, Long userId) {
        return groupUserRepository.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(groupId, userId);
    }

    public Page<GroupUser> findAllByGroupIdAndDeletedTimeIsNull(Pageable pageable, Long groupId) {
        return groupUserRepository.findAllByGroupIdAndDeletedTimeIsNull(pageable, groupId);
    }
}
