package com.yil.account.user.model;

import com.yil.account.base.AbstractEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "UserType")
public class UserType extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "UserType_Sequence_Generator",
            sequenceName = "Seq_UserType",
            allocationSize = 1)
    @GeneratedValue(generator = "UserType_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "RealPerson", nullable = false)
    private Boolean realPerson;

}
