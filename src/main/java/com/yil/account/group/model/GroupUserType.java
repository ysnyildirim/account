/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.group.model;


import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 1-Admin
 * 2-Manager
 * 3-User
 */
@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(schema = "GRP", name = "GROUP_USER_TYPE")
public class GroupUserType implements IEntity {
    @Id
    @SequenceGenerator(name = "GROUP_USER_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP_USER_TYPE_ID", schema = "USR",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_USER_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 1000)
    private String description;
}
