package com.yil.account.user.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.exception.UserTypeNotFoundException;
import com.yil.account.user.dto.CreateUserTypeDto;
import com.yil.account.user.dto.UserTypeDto;
import com.yil.account.user.model.UserType;
import com.yil.account.user.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "v1/user-types")
public class UserTypeController {

    private final UserTypeService userTypeService;

    @GetMapping
    public ResponseEntity<List<UserTypeDto>> findAll( ) {
        List<UserType> data = userTypeService.findAll();
        List<UserTypeDto> dtoData = new ArrayList<>();
        data.forEach(f -> {
            dtoData.add(UserTypeService.toDto(f));
        });
        return ResponseEntity.ok(dtoData);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserTypeDto> findById(@PathVariable Integer id) throws UserTypeNotFoundException {
        UserType userType = userTypeService.findById(id);
        UserTypeDto dto = UserTypeService.toDto(userType);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserTypeDto> create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                              @Valid @RequestBody CreateUserTypeDto request) {
        UserType userType = new UserType();
        userType.setName(request.getName());
        userType = userTypeService.save(userType);
        UserTypeDto dto = UserTypeService.toDto(userType);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserTypeDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Integer id,
                                               @Valid @RequestBody CreateUserTypeDto request) throws UserTypeNotFoundException {
        UserType userType = userTypeService.findById(id);
        userType.setName(request.getName());
        userType = userTypeService.save(userType);
        UserTypeDto dto = UserTypeService.toDto(userType);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Integer id) throws UserTypeNotFoundException {
        userTypeService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
