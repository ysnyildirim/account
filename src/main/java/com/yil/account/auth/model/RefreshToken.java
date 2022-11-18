/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "AUTH",
        name = "REFRESH_TOKEN")
public class RefreshToken {
    @Id
    @Column(name = "TOKEN")
    private String token;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Comment(value = "Son kullanım tarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRY_DATE", nullable = false)
    private Date expiryDate;
}
