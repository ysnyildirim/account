package com.yil.authentication.group.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.group.dto.CreateGroupUserDto;
import com.yil.authentication.group.dto.GroupUserDto;
import com.yil.authentication.group.model.GroupUser;
import com.yil.authentication.group.service.GroupUserService;
import com.yil.authentication.user.model.User;
import com.yil.authentication.user.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "v1/groups/{groupId}/users")
public class GroupUserController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final GroupUserService groupUserService;
    private final UserService userService;

    @Autowired
    public GroupUserController(GroupUserService groupUserService, UserService userService) {
        this.groupUserService = groupUserService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<GroupUserDto>> findAll(@PathVariable Long groupId) {
        try {
            List<GroupUser> data = groupUserService.findAllByGroupIdAndDeletedTimeIsNull(groupId);
            List<GroupUserDto> dtoData = new ArrayList<>();
            for (GroupUser groupUser : data) {
                GroupUserDto dto = GroupUserService.toDto(groupUser);
                dtoData.add(dto);
            }
            return ResponseEntity.ok(dtoData);
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @Valid @RequestBody CreateGroupUserDto dto) {
        try {
            User user = userService.findById(dto.getUserId());
            List<GroupUser> groupUsers = groupUserService.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(groupId, dto.getUserId());
            if (!groupUsers.isEmpty())
                return ResponseEntity.created(null).build();
            GroupUser groupUser = new GroupUser();
            groupUser.setGroupId(groupId);
            groupUser.setUserId(user.getId());
            groupUser.setCreatedUserId(authenticatedUserId);
            groupUser.setCreatedTime(new Date());
            groupUser = groupUserService.save(groupUser);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long id) {
        try {
            List<GroupUser> groupUsers;
            try {
                groupUsers = groupUserService.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(groupId, id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            for (GroupUser groupUser : groupUsers) {
                groupUser.setDeletedUserId(authenticatedUserId);
                groupUser.setDeletedTime(new Date());
            }
            groupUserService.saveAll(groupUsers);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
