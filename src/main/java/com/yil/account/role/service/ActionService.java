package com.yil.account.role.service;

import com.yil.account.exception.ActionNotFoundException;
import com.yil.account.role.dto.ActionDto;
import com.yil.account.role.model.Action;
import com.yil.account.role.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ActionService {
    private final ActionRepository actionRepository;

    @Autowired
    public ActionService(ActionRepository actionRepository) {
        this.actionRepository = actionRepository;
    }

    public static ActionDto toDto(Action action) {
        if (action == null)
            throw new NullPointerException();
        return ActionDto.builder()
                .id(action.getId())
                .value(action.getValue())
                .build();
    }

    public Action findById(Long id) throws ActionNotFoundException {
        return actionRepository.findById(id).orElseThrow(() -> new ActionNotFoundException());
    }

    public Action findByIdAndDeletedTimeIsNull(Long id) throws ActionNotFoundException {
        Action action = actionRepository.findByIdAndDeletedTimeIsNull(id);
        if (action == null)
            throw new ActionNotFoundException();
        return action;
    }

    public Action save(Action user) {
        return actionRepository.save(user);
    }

    public void delete(Long id) {
        actionRepository.deleteById(id);
    }

    public Page<Action> findAllByDeletedTimeIsNull(Pageable pageable) {
        return actionRepository.findAllByDeletedTimeIsNull(pageable);
    }
}
