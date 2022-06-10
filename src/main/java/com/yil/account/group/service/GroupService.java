package com.yil.account.group.service;

import com.yil.account.exception.GroupNotFoundException;
import com.yil.account.group.dto.GroupDto;
import com.yil.account.group.model.Group;
import com.yil.account.group.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public static GroupDto toDto(Group group) {
        if (group == null)
            throw new NullPointerException();
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .build();
    }

    public Group findById(Long id) throws GroupNotFoundException {
        return groupRepository.findById(id).orElseThrow(() -> new GroupNotFoundException());
    }

    public Group findByIdAndDeletedTimeIsNull(Long id) throws GroupNotFoundException {
        Group group = groupRepository.findByIdAndDeletedTimeIsNull(id);
        if (group == null)
            throw new GroupNotFoundException();
        return group;
    }

    public Group findByName(String name) throws GroupNotFoundException {
        Group group = groupRepository.findByNameAndDeletedTimeIsNull(name);
        if (group == null)
            throw new GroupNotFoundException();
        return group;
    }

    public boolean existsByNameAndDeletedTimeIsNull(String name) {
        return groupRepository.existsByNameAndDeletedTimeIsNull(name);
    }

    public Group save(Group user) {
        return groupRepository.save(user);
    }

    public void delete(Long id) {
        groupRepository.deleteById(id);
    }

    public Page<Group> findAllByDeletedTimeIsNull(Pageable pageable) {
        return groupRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
