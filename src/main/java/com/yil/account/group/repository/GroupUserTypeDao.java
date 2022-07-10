/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.group.repository;

import com.yil.account.group.model.Group;
import com.yil.account.group.model.GroupUserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupUserTypeDao extends JpaRepository<GroupUserType, Integer> {
}
