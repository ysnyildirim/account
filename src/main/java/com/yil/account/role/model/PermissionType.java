package com.yil.account.role.model;

import com.yil.account.base.IEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Data
@Table(name = "PERMISSION_TYPE",
        schema = "RL")
public class PermissionType implements IEntity {
    @Id
    @SequenceGenerator(name = "PERMISSION_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PERMISSION_TYPE_ID",
            schema = "RL")
    @GeneratedValue(generator = "PERMISSION_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
}
