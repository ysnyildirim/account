package com.yil.account.group.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.group.dto.CreateGroupUserDto;
import com.yil.account.group.dto.GroupUserDto;
import com.yil.account.group.model.GroupUser;
import com.yil.account.group.service.GroupUserService;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account/v1/groups/{groupId}/users")
public class GroupUserController {

    private final GroupUserService groupUserService;
    private final UserService userService;

    @Autowired
    public GroupUserController(GroupUserService groupUserService, UserService userService) {
        this.groupUserService = groupUserService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<GroupUserDto>> findAll(@PathVariable Long groupId) {
        List<GroupUser> data = groupUserService.findAllByGroupIdAndDeletedTimeIsNull(groupId);
        List<GroupUserDto> dtoData = new ArrayList<>();
        for (GroupUser groupUser : data) {
            GroupUserDto dto = GroupUserService.toDto(groupUser);
            dtoData.add(dto);
        }
        return ResponseEntity.ok(dtoData);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GroupUserDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long groupId,
                                               @Valid @RequestBody CreateGroupUserDto dto) throws UserNotFoundException {
        User user = userService.findById(dto.getUserId());
        List<GroupUser> groupUsers = groupUserService.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(groupId, dto.getUserId());
        GroupUserDto responce;
        if (!groupUsers.isEmpty()) {
            responce = GroupUserService.toDto(groupUsers.get(0));
            return ResponseEntity.created(null).body(responce);
        }
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(groupId);
        groupUser.setUserId(user.getId());
        groupUser.setCreatedUserId(authenticatedUserId);
        groupUser.setCreatedTime(new Date());
        groupUser = groupUserService.save(groupUser);
        responce = GroupUserService.toDto(groupUser);
        return ResponseEntity.created(null).body(responce);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long id) {
        List<GroupUser> groupUsers = groupUserService.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(groupId, id);
        if (groupUsers.isEmpty())
            return ResponseEntity.ok().build();
        for (GroupUser groupUser : groupUsers) {
            groupUser.setDeletedUserId(authenticatedUserId);
            groupUser.setDeletedTime(new Date());
        }
        groupUserService.saveAll(groupUsers);
        return ResponseEntity.ok().build();
    }

}
