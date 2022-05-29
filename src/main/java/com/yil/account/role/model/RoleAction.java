package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "RoleAction")
public class RoleAction extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "RoleAction_Sequence_Generator",
            sequenceName = "Seq_RoleAction",
            allocationSize = 1)
    @GeneratedValue(generator = "RoleAction_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "RoleId", nullable = false)
    private Long roleId;
    @Column(name = "ActionId", nullable = false)
    private Long actionId;
}
