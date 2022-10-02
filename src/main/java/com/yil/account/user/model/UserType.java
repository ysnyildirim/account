package com.yil.account.user.model;

import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "USR", name = "USER_TYPE")
public class UserType implements IEntity {
    @Id
    @SequenceGenerator(schema = "USR",
            name = "USER_TYPE_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_TYPE_ID")
    @GeneratedValue(generator = "USER_TYPE_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_MODIFY_DATE")
    private Date lastModifyDate;
    @Column(name = "LAST_MODIFY_USER_ID")
    private Long lastModifyUserId;

}
