package com.yil.account.user.model;

import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "USR", name = "USER")
public class User implements IEntity {
    @Id
    @SequenceGenerator(schema = "USR",
            name = "USER_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_ID")
    @GeneratedValue(generator = "USER_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_NAME", nullable = false, unique = true, length = 100)
    private String userName;
    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;
    @Column(name = "USER_TYPE_ID", nullable = false)
    private Integer userTypeId;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "1")
    @Column(name = "ENABLED", nullable = false)
    private boolean enabled;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "LOCKED", nullable = false)
    private boolean locked;
    @Column(name = "MAIL", nullable = false)
    private String mail;
    @Column(name = "PERSON_ID")
    private Long personId;
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "1")
    @Column(name = "PASSWORD_NEEDS_CHANGED", nullable = false)
    private boolean passwordNeedsChanged;
    @Column(name = "LAST_PASSWORD_CHANGE_TIME")
    private Date lastPasswordChangeTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME")
    private Date createdTime;
    @Column(name = "CREATED_USER_ID")
    private Long createdUserId;

}
