package com.yil.account.group.model;

import com.yil.account.base.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "GROUP_USER",
        indexes = {
                @Index(name = "IDX_GROUP_USER_USER_ID", columnList = "USER_ID"),
                @Index(name = "IDX_GROUP_USER_GROUP_USER_TYPE_ID", columnList = "GROUP_USER_TYPE_ID"),
                @Index(name = "IDX_GROUP_USER_GROUP_GROUP_ID", columnList = "GROUP_ID")
        },
        uniqueConstraints = @UniqueConstraint(name = "UNQ_CONS_GROUP_USER_GROUP", columnNames = {"USER_ID", "GROUP_USER_TYPE_ID", "GROUP_ID"}))
public class GroupUser extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "GROUP_USER_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP_USER_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_USER_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;
    @Column(name = "GROUP_USER_TYPE_ID", nullable = false)
    private Integer groupUserTypeId;
}
