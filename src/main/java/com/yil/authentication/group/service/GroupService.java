package com.yil.authentication.group.service;

import com.yil.authentication.group.dto.GroupDto;
import com.yil.authentication.group.model.Group;
import com.yil.authentication.group.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public static GroupDto toDto(Group group) {
        if (group == null)
            throw new NullPointerException();
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .build();
    }

    public Group findById(Long id) throws EntityNotFoundException {
        return groupRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException("Group not found");
        });
    }

    public Group findByName(String name) throws EntityNotFoundException {
        Group group = groupRepository.findByNameAndDeletedTimeIsNull(name);
        if (group == null)
            throw new EntityNotFoundException("Group not found");
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
