package com.yil.account.user.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.base.PageDto;
import com.yil.account.exception.UserNotFoundException;
import com.yil.account.exception.UserPhotoNotFound;
import com.yil.account.user.dto.UserPhotoDto;
import com.yil.account.user.dto.UserPhotoRequest;
import com.yil.account.user.dto.UserPhotoResponse;
import com.yil.account.user.model.UserPhoto;
import com.yil.account.user.service.UserPhotoService;
import com.yil.account.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/acc/v1/users/{userId}/photos")
public class UserPhotoController {
    private final UserPhotoService userPhotoService;
    private final UserService userService;
    private final Mapper<UserPhoto, UserPhotoDto> mapper = new Mapper<>(UserPhotoService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PageDto<UserPhotoDto>> findAll(@PathVariable Long userId,
                                                         @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(mapper.map(userPhotoService.findAllByUserId(pageable, userId)));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserPhotoDto> findById(@PathVariable Long userId, @PathVariable Long id) throws UserPhotoNotFound {
        return ResponseEntity.ok(mapper.map(userPhotoService.findByIdAndUserId(id, userId)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserPhotoResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                    @PathVariable Long userId,
                                                    @Valid @NotNull @RequestBody UserPhotoRequest request) throws UserNotFoundException {
        if (!userService.existsById(userId))
            throw new UserNotFoundException();
        UserPhoto userPhoto = new UserPhoto();
        userPhoto.setUserId(userId);
        userPhoto.setContent(request.getContent());
        userPhoto.setExtension(request.getExtension());
        userPhoto.setName(request.getName());
        userPhoto = userPhotoService.save(userPhoto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserPhotoResponse.builder().id(userPhoto.getId()).build());
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserPhotoResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                     @PathVariable Long userId,
                                                     @PathVariable Long id,
                                                     @Valid @NotNull @RequestBody UserPhotoRequest request) throws UserPhotoNotFound {
        UserPhoto userPhoto = userPhotoService.findByIdAndUserId(id, userId);
        userPhoto.setName(request.getName());
        userPhoto.setExtension(request.getExtension());
        userPhoto.setContent(request.getContent());
        userPhoto = userPhotoService.save(userPhoto);
        return ResponseEntity.ok(UserPhotoResponse.builder().id(userPhoto.getId()).build());
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long userId,
                                 @PathVariable Long id) {
        userPhotoService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
