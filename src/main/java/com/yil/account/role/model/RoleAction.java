package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ROLE_ACTION")
public class RoleAction extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ROLE_ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ROLE_ACTION",
            allocationSize = 1)
    @GeneratedValue(generator = "ROLE_ACTION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
}
