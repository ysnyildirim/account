package com.yil.account.group.model;

import com.yil.account.base.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "GROUP_ROLE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRole extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "GROUP_ROLE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP_ROLE",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_ROLE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "GROUP_ID", nullable = false)
    private Long groupId;
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
}
