package com.yil.account.user.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.PageDto;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.exception.UserPhotoNotFound;
import com.yil.account.user.dto.CreateUserPhotoDto;
import com.yil.account.user.dto.UserPhotoDto;
import com.yil.account.user.model.User;
import com.yil.account.user.model.UserPhoto;
import com.yil.account.user.service.UserPhotoService;
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
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/users/{userId}/photos")
public class UserPhotoController {

    private final UserPhotoService userPhotoService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<PageDto<UserPhotoDto>> findAll(@PathVariable Long userId,
                                                         @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                         @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<UserPhoto> data = userPhotoService.findAllByUserIdAndDeletedTimeIsNull(pageable, userId);
        List<UserPhotoDto> dtoData = new ArrayList<>();
        for (UserPhoto userPhoto : data.getContent()) {
            UserPhotoDto dto = userPhotoService.toDto(userPhoto);
            dtoData.add(dto);
        }
        PageDto<UserPhotoDto> pageDto = new PageDto<>();
        pageDto.setTotalElements(data.getTotalElements());
        pageDto.setCurrentPage(data.getNumber());
        pageDto.setTotalPages(data.getTotalPages());
        pageDto.setContent(dtoData);
        return ResponseEntity.ok(pageDto);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserPhotoDto> findById(@PathVariable Long id, @PathVariable Long userId) throws UserPhotoNotFound {
        UserPhoto userPhoto = userPhotoService.findByIdAndUserIdAndDeletedTimeIsNull(id, userId);
        UserPhotoDto dto = UserPhotoService.toDto(userPhoto);
        return ResponseEntity.ok(dto);
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserPhotoDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long userId,
                                               @Valid @RequestBody CreateUserPhotoDto request) throws UserNotFoundException {
        User user = userService.findByIdAndDeletedTimeIsNull(userId);
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserId(user.getId());
        userPhoto.setContent(request.getContent());
        userPhoto.setExtension(request.getExtension());
        userPhoto.setName(request.getName());
        userPhoto.setCreatedUserId(authenticatedUserId);
        userPhoto.setCreatedTime(new Date());
        userPhoto = userPhotoService.save(userPhoto);
        UserPhotoDto dto = UserPhotoService.toDto(userPhoto);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserPhotoDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody CreateUserPhotoDto request, @PathVariable Long userId) throws UserPhotoNotFound {
        UserPhoto userPhoto = userPhotoService.findByIdAndUserIdAndDeletedTimeIsNull(id, userId);
        userPhoto.setName(request.getName());
        userPhoto.setExtension(request.getExtension());
        userPhoto.setContent(request.getContent());
        userPhoto = userPhotoService.save(userPhoto);
        UserPhotoDto dto = UserPhotoService.toDto(userPhoto);
        return ResponseEntity.ok(dto);
    }


    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long userId,
                                 @PathVariable Long id) throws UserPhotoNotFound {
        UserPhoto userPhoto = userPhotoService.findByIdAndUserIdAndDeletedTimeIsNull(id, userId);
        userPhoto.setDeletedUserId(authenticatedUserId);
        userPhoto.setDeletedTime(new Date());
        userPhotoService.save(userPhoto);
        return ResponseEntity.ok().build();
    }

}
