package com.yil.account.user.model;

import com.yil.account.base.IEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "USR",
        name = "USER",
        uniqueConstraints = @UniqueConstraint(name = "UC_USER_USER_NAME", columnNames = {"USER_NAME"}))
public class User implements IEntity {
    @Id
    @SequenceGenerator(schema = "USR",
            name = "USER_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_ID")
    @GeneratedValue(generator = "USER_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Comment("Kullanıcı adı")
    @Column(name = "USER_NAME", nullable = false, length = 100)
    private String userName;
    @Comment("Şifre")
    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;
    @Comment("Kullanıcı kilit durumu")
    @Column(name = "LOCKED", nullable = false)
    private boolean locked;
    @Comment("Kullanıcı mail bilgisi. Şifre yenileme işlemleri bu mail üzerinden sağlanıcaktır.")
    @Column(name = "MAIL", nullable = false)
    private String mail;
    @Comment("Şifre süreli mi? Süreli ise belirli periyotlarda değiştirilmesi gerekli")
    @Column(name = "EXPIRED_PASSWORD", nullable = false)
    private boolean expiredPassword;
    @Comment("Son şifre değişiklik tarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_PASSWORD_CHANGE")
    private Date lastPasswordChange;
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
