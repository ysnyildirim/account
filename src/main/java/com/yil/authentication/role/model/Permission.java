package com.yil.authentication.role.model;

import com.yil.authentication.base.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Permission")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Permission extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Permission_Sequence_Generator",
            sequenceName = "Seq_Permission",
            allocationSize = 1)
    @GeneratedValue(generator = "Permission_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name", nullable = false, length = 1000)
    private String name;
}
