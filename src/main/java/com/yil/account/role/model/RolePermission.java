package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ROLE_PERMİSSİON")
public class RolePermission extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ROLE_PERMISSION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ROLE_PERMISSION",
            allocationSize = 1)
    @GeneratedValue(generator = "ROLE_PERMISSION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
    @Column(name = "PERMISSION_ID", nullable = false)
    private Long permissionId;
}
