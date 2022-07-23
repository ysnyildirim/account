/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.role.service;

import com.yil.account.role.repository.PermissionTypeDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionTypeService {


    public static final int System = 1;
    public static final int Workflow = 2;
    public static final int Another = 3;


    private final PermissionTypeDao permissionTypeDao;

    public boolean existsById(int id) {
        return permissionTypeDao.existsById(id);
    }

}
