package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "RoleId_Sequence_Generator",
            sequenceName = "Seq_RoleId",
            allocationSize = 1)
    @GeneratedValue(generator = "RoleId_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name", nullable = false, length = 1000)
    private String name;
    @Column(name = "Description", nullable = false, length = 4000)
    private String description;
}
