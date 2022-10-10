package com.yil.account.user.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.Mapper;
import com.yil.account.exception.UserTypeNotFoundException;
import com.yil.account.user.dto.UserTypeDto;
import com.yil.account.user.dto.UserTypeRequest;
import com.yil.account.user.dto.UserTypeResponse;
import com.yil.account.user.model.UserType;
import com.yil.account.user.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/acc/v1/user-types")
public class UserTypeController {
    private final UserTypeService userTypeService;
    private final Mapper<UserType, UserTypeDto> mapper = new Mapper<>(UserTypeService::toDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserTypeDto>> findAll() {
        return ResponseEntity.ok(mapper.map(userTypeService.findAll()));
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserTypeDto> findById(@PathVariable Integer id) throws UserTypeNotFoundException {
        return ResponseEntity.ok(mapper.map(userTypeService.findById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTypeResponse> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                   @Valid @RequestBody UserTypeRequest request) {
        UserType userType = UserType
                .builder()
                .name(request.getName())
                .createdDate(new Date())
                .createdUserId(authenticatedUserId)
                .build();
        userType = userTypeService.save(userType);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserTypeResponse
                        .builder()
                        .id(userType.getId())
                        .build());
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserTypeResponse> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                                    @PathVariable Integer id,
                                                    @Valid @RequestBody UserTypeRequest request) throws UserTypeNotFoundException {
        UserType userType = userTypeService.findById(id);
        userType.setName(request.getName());
        userType = userTypeService.save(userType);
        return ResponseEntity
                .ok(UserTypeResponse
                        .builder()
                        .id(userType.getId())
                        .build());
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Integer id) {
        userTypeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
