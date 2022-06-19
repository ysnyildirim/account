package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "PERMISSION")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Permission extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "PERMISSION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PERMISSION_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "PERMISSION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 1000)
    private String name;
}
