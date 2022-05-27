package com.yil.authentication.user.controller;

import com.yil.authentication.base.ApiHeaders;
import com.yil.authentication.user.dto.CreateUserTypeDto;
import com.yil.authentication.user.dto.UserTypeDto;
import com.yil.authentication.user.model.UserType;
import com.yil.authentication.user.service.UserTypeService;
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
        try {
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
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<UserTypeDto> findById(@PathVariable Long id) {
        try {
            UserType userType = userTypeService.findById(id);
            UserTypeDto dto = UserTypeService.toDto(userType);
            return ResponseEntity.ok(dto);
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @Valid @RequestBody CreateUserTypeDto dto) {
        try {
            UserType userType = new UserType();
            userType.setName(dto.getName());
            userType.setRealPerson(dto.getRealPerson());
            userType.setCreatedUserId(authenticatedUserId);
            userType.setCreatedTime(new Date());
            userType = userTypeService.save(userType);
            return ResponseEntity.created(null).build();
        } catch (Exception exception) {

            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity replace(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                  @PathVariable Long id,
                                  @Valid @RequestBody CreateUserTypeDto dto) {
        try {
            UserType userType;
            try {
                userType = userTypeService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            userType.setRealPerson(dto.getRealPerson());
            userType.setName(dto.getName());
            userType = userTypeService.save(userType);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> delete(@RequestHeader(value = ApiHeaders.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                         @PathVariable Long id) {
        try {
            UserType userType;
            try {
                userType = userTypeService.findById(id);
            } catch (EntityNotFoundException entityNotFoundException) {
                return ResponseEntity.notFound().build();
            } catch (Exception e) {
                throw e;
            }
            userType.setDeletedUserId(authenticatedUserId);
            userType.setDeletedTime(new Date());
            userTypeService.save(userType);
            return ResponseEntity.ok().build();
        } catch (Exception exception) {
            logger.error(null, exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
