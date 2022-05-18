package com.yil.authentication.group.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.base.PageDto;
import com.yil.authentication.group.controller.dto.CreateGroupDto;
import com.yil.authentication.group.controller.dto.GroupDto;
import com.yil.authentication.group.model.Group;
import com.yil.authentication.group.service.GroupService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.Date;
import java.util.Objects;

@RestController
@RequestMapping("v1/groups")
public class GroupController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<PageDto<GroupDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            if (page < 0)
                page = 0;
            if (size <= 0 || size > 1000)
                size = 1000;
            Pageable pageable = PageRequest.of(page, size);
            Page<Group> groupPage = groupService.findAllByDeletedTimeIsNull(pageable);
            PageDto<GroupDto> pageDto = PageDto.toDto(groupPage, GroupService::toDto);
            return ResponseEntity.ok(pageDto);
        } catch (Exception exception) {

            logger.error(null, exception);
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<GroupDto> findById(@PathVariable Long id) {
        try {
            Objects.requireNonNull(id, "Id is null");
            Group entity = groupService.findById(id);
            GroupDto dto = GroupService.toDto(entity);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateGroupDto dto) {
        try {
            if (groupService.existsByNameAndDeletedTimeIsNull(dto.getName()))
                return ResponseEntity.badRequest().build();
            Group entity = Group.builder()
                    .name(dto.getName())
                    .build();
            entity.setCreatedUserId(authenticatedUserId);
            entity.setCreatedTime(new Date());
            entity = groupService.save(entity);
            URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(entity.getId())
                    .toUri();
            return ResponseEntity.created(uri).build();
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateGroupDto dto) {
        try {
            Group group;
            try {
                group = groupService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            if (groupService.existsByNameAndDeletedTimeIsNull(dto.getName()))
                return ResponseEntity.badRequest().build();
            group.setName(dto.getName());
            group = groupService.save(group);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long id) {
        try {
            Group entity;
            try {
                entity = groupService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            entity.setDeletedUserId(authenticatedUserId);
            entity.setDeletedTime(new Date());
            groupService.save(entity);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
