package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "ROLE_ACTİON")
public class RoleAction extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ROLE_ACTİON_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ROLE_ACTİON",
            allocationSize = 1)
    @GeneratedValue(generator = "ROLE_ACTİON_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
    @Column(name = "ACTION_ID", nullable = false)
    private Long actionId;
}
