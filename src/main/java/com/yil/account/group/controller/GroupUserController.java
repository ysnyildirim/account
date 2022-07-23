package com.yil.account.group.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.base.PageDto;
import com.yil.account.exception.GroupNotFoundException;
import com.yil.account.exception.GroupUserNotFound;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.group.dto.CreateGroupUserDto;
import com.yil.account.group.model.GroupUser;
import com.yil.account.group.service.GroupService;
import com.yil.account.group.service.GroupUserService;
import com.yil.account.user.dto.UserDto;
import com.yil.account.user.model.User;
import com.yil.account.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/groups/{groupId}/users")
public class GroupUserController {

    private final GroupUserService groupUserService;
    private final UserService userService;
    private final GroupService groupService;
    private final Mapper<User, UserDto> userMapper = new Mapper<User, UserDto>(UserService::convert);

    @GetMapping
    public ResponseEntity<PageDto<UserDto>> findAll(@PathVariable Long groupId,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                    @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) throws UserNotFoundException {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<GroupUser> data = groupUserService.findAllById_GroupId(pageable, groupId);
        List<UserDto> dtoData = new ArrayList<>();
        for (GroupUser groupUser : data.getContent()) {
            User user = userService.findById(groupUser.getId().getUserId());
            dtoData.add(userMapper.map(user));
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
        if (groupService.existsById(groupId))
            throw new GroupNotFoundException();
        if (userService.existsById(groupId))
            throw new UserNotFoundException();
        GroupUser.Pk id = GroupUser.Pk.builder().groupId(groupId).userId(dto.getUserId()).groupUserTypeId(dto.getGroupUserTypeId()).build();
        if (!groupUserService.existsById(id)) {
            GroupUser groupUser = new GroupUser();
            groupUser.setId(id);
            groupUser = groupUserService.save(groupUser);
        }
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/user-id={userId}&group-user-type-id={groupUserTypeId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long groupId,
                                 @PathVariable Long userId,
                                 @PathVariable Integer groupUserTypeId) throws GroupUserNotFound {
        GroupUser.Pk id = GroupUser.Pk.builder().groupId(groupId).userId(userId).groupUserTypeId(groupUserTypeId).build();
        groupUserService.delete(id);
        return ResponseEntity.ok().build();
    }

}
