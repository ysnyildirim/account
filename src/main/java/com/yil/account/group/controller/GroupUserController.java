package com.yil.account.group.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.PageDto;
import com.yil.account.exception.GroupNotFoundException;
import com.yil.account.exception.GroupUserNotFound;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.group.dto.CreateGroupUserDto;
import com.yil.account.group.model.Group;
import com.yil.account.group.model.GroupUser;
import com.yil.account.group.service.GroupService;
import com.yil.account.group.service.GroupUserService;
import com.yil.account.user.dto.UserDto;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final GroupService groupService;

    @Autowired
    public GroupUserController(GroupUserService groupUserService, UserService userService, GroupService groupService) {
        this.groupUserService = groupUserService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<PageDto<UserDto>> findAll(@PathVariable Long groupId,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) throws UserNotFoundException {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupUser> data = groupUserService.findAllByGroupIdAndDeletedTimeIsNull(pageable, groupId);
        List<UserDto> dtoData = new ArrayList<>();
        for (GroupUser groupUser : data.getContent()) {
            User user = userService.findById(groupUser.getUserId());
            UserDto userDto = UserService.toDto(user);
            dtoData.add(userDto);
        }
        PageDto<UserDto> pageDto = new PageDto<>();
        pageDto.setTotalElements(data.getTotalElements());
        pageDto.setCurrentPage(data.getNumber());
        pageDto.setTotalPages(data.getTotalPages());
        pageDto.setContent(dtoData);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @Valid @RequestBody CreateGroupUserDto dto) throws UserNotFoundException, GroupNotFoundException {
        Group group = groupService.findByIdAndDeletedTimeIsNull(groupId);
        User user = userService.findByIdAndDeletedTimeIsNull(dto.getUserId());
        List<GroupUser> groupUsers = groupUserService.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(group.getId(), user.getId());
        if (!groupUsers.isEmpty())
            return ResponseEntity.created(null).build();
        GroupUser groupUser = new GroupUser();
        groupUser.setGroupId(group.getId());
        groupUser.setUserId(user.getId());
        groupUser.setCreatedUserId(authenticatedUserId);
        groupUser.setCreatedTime(new Date());
        groupUser = groupUserService.save(groupUser);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long id) throws GroupUserNotFound {
        List<GroupUser> groupUsers = groupUserService.findAllByGroupIdAndUserIdAndDeletedTimeIsNull(groupId, id);
        if (groupUsers.isEmpty())
            throw new GroupUserNotFound();
        for (GroupUser groupUser : groupUsers) {
            groupUser.setDeletedUserId(authenticatedUserId);
            groupUser.setDeletedTime(new Date());
        }
        groupUserService.saveAll(groupUsers);
        return ResponseEntity.ok().build();
    }

}
