package com.yil.account.role.model;

import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "RL", name = "ROLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements IEntity {
    @Id
    @SequenceGenerator(name = "ROLE_ID_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ROLE_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "ROLE_ID_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "PARENT_ID")
    private Long parentId;
    @Column(name = "NAME", nullable = false, length = 1000)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 4000)
    private String description;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "ASSIGNABLE", nullable = false)
    private boolean assignable;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "INTERITABLE", nullable = false)
    private boolean inheritable;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME")
    private Date createdTime;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
}
