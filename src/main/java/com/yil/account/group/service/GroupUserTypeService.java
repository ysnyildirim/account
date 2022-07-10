/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.group.service;

import com.yil.account.exception.GroupUserNotFoundException;
import com.yil.account.group.dto.GroupUserTypeDto;
import com.yil.account.group.model.GroupUserType;
import com.yil.account.group.repository.GroupUserTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GroupUserTypeService {

    public static final int Admin = 1;
    public static final int Manager = 2;
    public static final int User = 3;

    private final GroupUserTypeDao groupUserTypeDao;

    private static GroupUserTypeDto convert(GroupUserType groupUserType) {
        GroupUserTypeDto dto = new GroupUserTypeDto();
        dto.setId(groupUserType.getId());
        dto.setName(groupUserType.getName());
        dto.setDescription(groupUserType.getDescription());
        return dto;
    }

    @Transactional(readOnly = true)
    public GroupUserType findById(Integer id) throws GroupUserNotFoundException {
        return groupUserTypeDao.findById(id).orElseThrow(GroupUserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public boolean existsById(Integer id) {
        return groupUserTypeDao.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<GroupUserType> findAll() {
        return groupUserTypeDao.findAll();
    }

}
