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
@Table(name = "GROUP_USER")
public class GroupUser extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "GROUP_USER_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP_USER",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_USER_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;
}
