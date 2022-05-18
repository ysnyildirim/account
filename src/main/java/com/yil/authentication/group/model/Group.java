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
@Table(name = "\"Group\"")
public class Group extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "Group_Sequence_Generator",
            sequenceName = "Seq_Group",
            allocationSize = 1)
    @GeneratedValue(generator = "Group_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name", nullable = false, unique = true, length = 100)
    private String name;
}
