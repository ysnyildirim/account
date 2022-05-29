package com.yil.account.user.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.exception.UserTypeNotFoundException;
import com.yil.account.user.dto.CreateUserTypeDto;
import com.yil.account.user.dto.UserTypeDto;
import com.yil.account.user.model.UserType;
import com.yil.account.user.service.UserTypeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "v1/user-types")
public class UserTypeController {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final UserTypeService userTypeService;

    @Autowired
    public UserTypeController(UserTypeService userTypeService) {
        this.userTypeService = userTypeService;
    }

    @GetMapping
    public ResponseEntity<List<UserTypeDto>> findAll(@RequestParam(required = false) Boolean realPerson) {
        List<UserType> data;
        if (realPerson != null)
            data = userTypeService.findAllByRealPersonAndDeletedTimeIsNull(realPerson);
        else
            data = userTypeService.findAllByDeletedTimeIsNull();
        List<UserTypeDto> dtoData = new ArrayList<>();
        data.forEach(f -> {
            dtoData.add(UserTypeService.toDto(f));
        });
        return ResponseEntity.ok(dtoData);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserTypeDto> findById(@PathVariable Long id) throws UserTypeNotFoundException {
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
        userType.setRealPerson(request.getRealPerson());
        userType.setCreatedUserId(authenticatedUserId);
        userType.setCreatedTime(new Date());
        userType = userTypeService.save(userType);
        UserTypeDto dto = UserTypeService.toDto(userType);
        return ResponseEntity.created(null).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserTypeDto> replace(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                               @PathVariable Long id,
                                               @Valid @RequestBody CreateUserTypeDto request) throws UserTypeNotFoundException {
        UserType userType = userTypeService.findByIdAndDeletedTimeIsNull(id);
        userType.setRealPerson(request.getRealPerson());
        userType.setName(request.getName());
        userType = userTypeService.save(userType);
        UserTypeDto dto = UserTypeService.toDto(userType);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) throws UserTypeNotFoundException {
        UserType userType = userTypeService.findByIdAndDeletedTimeIsNull(id);
        userType.setDeletedUserId(authenticatedUserId);
        userType.setDeletedTime(new Date());
        userTypeService.save(userType);
        return ResponseEntity.ok().build();
    }

}
