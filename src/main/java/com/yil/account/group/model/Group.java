package com.yil.account.group.model;

import com.yil.account.base.AbstractEntity;
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
@Table(name = "\"GROUP\"")
public class Group extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "GROUP_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_GROUP",
            allocationSize = 1)
    @GeneratedValue(generator = "GROUP_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, unique = true, length = 100)
    private String name;
}
