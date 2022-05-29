package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "RolePermission")
public class RolePermission extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "RolePermission_Sequence_Generator",
            sequenceName = "Seq_RolePermission",
            allocationSize = 1)
    @GeneratedValue(generator = "RolePermission_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "RoleId", nullable = false)
    private Long roleId;
    @Column(name = "PermissionId", nullable = false)
    private Long permissionId;
}
