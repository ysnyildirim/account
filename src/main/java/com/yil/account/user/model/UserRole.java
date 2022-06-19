package com.yil.account.user.model;

import com.yil.account.base.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "USER_ROLE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "USER_ROLE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_ROLE_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "USER_ROLE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "ROLE_ID", nullable = false)
    private Long roleId;
}
