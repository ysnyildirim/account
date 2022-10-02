package com.yil.account.group.service;

import com.yil.account.exception.GroupUserNotFoundException;
import com.yil.account.group.dto.GroupUserDto;
import com.yil.account.group.model.GroupUser;
import com.yil.account.group.repository.GroupUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupUserService {
    private final GroupUserRepository groupUserRepository;

    public static GroupUserDto convert(@NotNull GroupUser f) {
        GroupUserDto dto = new GroupUserDto();
        dto.setUserId(f.getId().getUserId());
        dto.setGroupId(f.getId().getUserId());
        return dto;
    }

    public GroupUser findById(GroupUser.Pk id) throws GroupUserNotFoundException {
        return groupUserRepository.findById(id).orElseThrow(() -> new GroupUserNotFoundException());
    }

    public boolean existsById(GroupUser.Pk id) {
        return groupUserRepository.existsById(id);
    }

    public GroupUser save(GroupUser groupUser) {
        return groupUserRepository.save(groupUser);
    }

    public List<GroupUser> saveAll(List<GroupUser> roles) {
        return groupUserRepository.saveAll(roles);
    }

    public void delete(GroupUser.Pk id) {
        groupUserRepository.deleteById(id);
    }

    public Page<GroupUser> findAllById_GroupId(Pageable pageable, Long groupId) {
        return groupUserRepository.findAllById_GroupId(pageable, groupId);
    }

    public List<GroupUser> findAllById_UserId(Long userId) {
        return groupUserRepository.findAllById_UserId(userId);
    }

    public List<Long> getGroupIdByUserId(long userId) {
        return groupUserRepository.getGroupIdByUserId(userId);
    }
}
