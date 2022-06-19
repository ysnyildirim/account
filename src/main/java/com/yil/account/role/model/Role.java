package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "ROLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ROLE_ID_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ROLE_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "ROLE_ID_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 1000)
    private String name;
    @Column(name = "DESCRIPTION", nullable = false, length = 4000)
    private String description;
    @Column(name = "ASSIGNABLE",nullable = false)
    private Boolean assignable;
}
