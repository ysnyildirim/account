package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "ACTION")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Action extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "ACTION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ACTION_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "ACTION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "VALUE", nullable = false, length = 1000)
    private String value;
}
