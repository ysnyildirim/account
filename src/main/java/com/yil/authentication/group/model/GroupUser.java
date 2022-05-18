package com.yil.authentication.group.model;

import com.yil.authentication.base.AbstractEntity;
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
@Table(name = "GroupUser")
public class GroupUser extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "GroupUser_Sequence_Generator",
            sequenceName = "Seq_GroupUser",
            allocationSize = 1)
    @GeneratedValue(generator = "GroupUser_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "UserId", nullable = false)
    private Long userId;
    @Column(name = "GroupId", nullable = false)
    private Long groupId;
}
