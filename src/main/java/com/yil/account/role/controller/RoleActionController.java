package com.yil.account.role.controller;

import com.yil.account.base.ApiConstant;
import com.yil.account.base.PageDto;
import com.yil.account.exception.ActionNotFoundException;
import com.yil.account.exception.RoleNotFoundException;
import com.yil.account.exception.RoleActionNotFound;
import com.yil.account.role.dto.CreateRoleActionDto;
import com.yil.account.role.dto.ActionDto;
import com.yil.account.role.model.Action;
import com.yil.account.role.model.Role;
import com.yil.account.role.model.RoleAction;
import com.yil.account.role.service.ActionService;
import com.yil.account.role.service.RoleActionService;
import com.yil.account.role.service.RoleService;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/account/v1/roles/{roleId}/actions")
public class RoleActionController {

    private final RoleActionService roleActionService;
    private final ActionService actionService;
    private final RoleService roleService;


    @GetMapping
    public ResponseEntity<PageDto<ActionDto>> findAll(@PathVariable Long roleId,
                                                          @RequestParam(required = false, defaultValue = ApiConstant.PAGE) int page,
                                                          @RequestParam(required = false, defaultValue = ApiConstant.PAGE_SIZE) int size) throws ActionNotFoundException {
        if (page < 0)
            page = 0;
        if (size <= 0 || size > 1000)
            size = 1000;
        Pageable pageable = PageRequest.of(page, size);
        Page<RoleAction> data = roleActionService.findAllByRoleIdAndDeletedTimeIsNull(pageable, roleId);
        List<ActionDto> dtoData = new ArrayList<>();
        for (RoleAction roleAction : data.getContent()) {
            Action action = actionService.findById(roleAction.getActionId());
            ActionDto actionDto = ActionService.toDto(action);
            dtoData.add(actionDto);
        }
        PageDto<ActionDto> pageDto = new PageDto<>();
        pageDto.setTotalElements(data.getTotalElements());
        pageDto.setCurrentPage(data.getNumber());
        pageDto.setTotalPages(data.getTotalPages());
        pageDto.setContent(dtoData);
        return ResponseEntity.ok(pageDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity create(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long roleId,
                                 @Valid @RequestBody CreateRoleActionDto dto) throws ActionNotFoundException, RoleNotFoundException {
        Role role = roleService.findByIdAndDeletedTimeIsNull(roleId);
        Action action = actionService.findById(dto.getActionId());
        List<RoleAction> roleActions = roleActionService.findAllByRoleIdAndActionIdAndDeletedTimeIsNull(role.getId(), action.getId());
        if (!roleActions.isEmpty())
            return ResponseEntity.created(null).build();
        RoleAction roleAction = new RoleAction();
        roleAction.setRoleId(role.getId());
        roleAction.setActionId(action.getId());
        roleAction.setCreatedUserId(authenticatedUserId);
        roleAction.setCreatedTime(new Date());
        roleAction = roleActionService.save(roleAction);
        return ResponseEntity.created(null).build();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity delete(@RequestHeader(value = ApiConstant.AUTHENTICATED_USER_ID) Long authenticatedUserId,
                                 @PathVariable Long roleId,
                                 @PathVariable Long id) throws RoleNotFoundException, ActionNotFoundException, RoleActionNotFound {
          List<RoleAction> roleActions = roleActionService.findAllByRoleIdAndActionIdAndDeletedTimeIsNull(roleId, id);
        if (roleActions.isEmpty())
            throw new RoleActionNotFound();
        for (RoleAction roleAction : roleActions) {
            roleAction.setDeletedUserId(authenticatedUserId);
            roleAction.setDeletedTime(new Date());
        }
        roleActionService.saveAll(roleActions);
        return ResponseEntity.ok().build();
    }

}
