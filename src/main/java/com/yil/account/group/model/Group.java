package com.yil.account.group.model;

import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "GRP", name = "\"GROUP\"")
public class Group implements IEntity {
    @Id
    @SequenceGenerator(name = "GROUP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;
    @Column(name = "DESCRIPTION", length = 100)
    private String description;
    @Column(name = "MAIL")
    private String email;
    @Column(name = "GROUP_TYPE_ID", nullable = false)
    private Integer groupTypeId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME")
    private Date createdTime;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
}
