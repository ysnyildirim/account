package com.yil.account.user.model;

import com.yil.account.base.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "UserRole")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "UserRole_Sequence_Generator",
            sequenceName = "Seq_UserRole",
            allocationSize = 1)
    @GeneratedValue(generator = "UserRole_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "UserId", nullable = false)
    private Long userId;
    @Column(name = "RoleId", nullable = false)
    private Long roleId;
}
