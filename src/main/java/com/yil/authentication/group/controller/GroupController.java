package com.yil.authentication.group.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.base.PageDto;
import com.yil.authentication.exception.GroupNameCannotBeUsed;
import com.yil.authentication.group.dto.CreateGroupDto;
import com.yil.authentication.group.dto.GroupDto;
import com.yil.authentication.group.model.Group;
import com.yil.authentication.group.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/api/account/v1/groups")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<PageDto<GroupDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<Group> groupPage = groupService.findAllByDeletedTimeIsNull(pageable);
        PageDto<GroupDto> pageDto = PageDto.toDto(groupPage, GroupService::toDto);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDto> findById(@PathVariable Long id) {
        Group entity = groupService.findById(id);
        GroupDto dto = GroupService.toDto(entity);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GroupDto> create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                           @Valid @RequestBody CreateGroupDto request) {
        if (groupService.existsByNameAndDeletedTimeIsNull(request.getName()))
            throw new GroupNameCannotBeUsed();
        Group entity = new Group();
        entity.setName(request.getName());
        entity.setCreatedUserId(authenticatedUserId);
        entity.setCreatedTime(new Date());
        entity = groupService.save(entity);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(entity.getId())
                .toUri();
        GroupDto responce = GroupService.toDto(entity);
        return ResponseEntity.created(uri).body(responce);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateGroupDto request) {
        Group group = groupService.findByIdAndDeletedTimeIsNull(id);
        if (groupService.existsByNameAndDeletedTimeIsNull(request.getName()))
            throw new GroupNameCannotBeUsed();
        group.setName(request.getName());
        group = groupService.save(group);
        GroupDto responce = GroupService.toDto(group);
        return ResponseEntity.ok(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        Group entity = groupService.findByIdAndDeletedTimeIsNull(id);
        entity.setDeletedUserId(authenticatedUserId);
        entity.setDeletedTime(new Date());
        groupService.save(entity);
        return ResponseEntity.ok().build();
    }

}
