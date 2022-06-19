package com.yil.account.user.model;

import com.yil.account.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "USER_TYPE")
public class UserType extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "USER_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_TYPE_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "USER_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "REAL_PERSON", nullable = false)
    private Boolean realPerson;

}
