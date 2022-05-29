package com.yil.account.role.model;

import com.yil.account.base.AbstractEntity;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@Table(name = "Action")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Action extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Action_Sequence_Generator",
            sequenceName = "Seq_Action",
            allocationSize = 1)
    @GeneratedValue(generator = "Action_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Value", nullable = false, length = 1000)
    private String value;
}
