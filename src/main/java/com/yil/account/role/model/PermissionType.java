/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.role.model;


import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(schema = "RL", name = "PERMISSION_TYPE")
public class PermissionType implements IEntity {
    @Id
    @SequenceGenerator(name = "PERMISSION_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PERMISSION_TYPE_ID", schema = "USR",
            allocationSize = 1)
    @GeneratedValue(generator = "PERMISSION_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
