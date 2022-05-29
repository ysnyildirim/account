package com.yil.account.group.model;

import com.yil.account.base.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "GroupRole")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRole extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "GroupRole_Sequence_Generator",
            sequenceName = "Seq_GroupRole",
            allocationSize = 1)
    @GeneratedValue(generator = "GroupRole_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "GroupId", nullable = false)
    private Long groupId;
    @Column(name = "RoleId", nullable = false)
    private Long roleId;
}
