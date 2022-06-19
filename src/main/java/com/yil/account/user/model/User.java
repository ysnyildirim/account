package com.yil.account.user.model;

import com.yil.account.base.AbstractEntity;
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
@Table(name = "USER" )
public class User extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "USER_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "USER_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_NAME", nullable = false, unique = true, length = 100)
    private String userName;
    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;
    @Column(name = "USER_TYPE_ID", nullable = false)
    private Long userTypeId;
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled;
    @Column(name = "LOCKED", nullable = false)
    private Boolean locked;
    @Column(name = "MAIL", nullable = false)
    private String mail;
    @Column(name = "PERSON_ID")
    private Long personId;
    @Column(name = "PASSWORD_NEEDS_CHANGED")
    private Boolean passwordNeedsChanged;
    @Column(name = "LAST_PASSWORD_CHANGE_TIME")
    private Date lastPasswordChangeTime;

}
