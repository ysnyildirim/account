package com.yil.account.role.model;

import com.yil.account.base.IEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "RL", name = "PERMISSION")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Permission implements IEntity {
    @Id
    @SequenceGenerator(name = "PERMISSION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PERMISSION_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "PERMISSION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 1000)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 4000)
    private String description;
    @Column(name = "PERMISSION_TYPE_ID", nullable = false)
    private Integer permissionTypeId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME")
    private Date createdTime;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
}
