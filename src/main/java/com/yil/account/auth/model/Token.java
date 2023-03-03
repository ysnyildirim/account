/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.account.auth.model;

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
@Table(schema = "AUTH",
        name = "TOKEN")
public class Token {
    @Id
    @Column(name = "TOKEN")
    private String token;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Comment(value = "Son kullanım tarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRY_DATE", nullable = false)
    private Date expiryDate;
    @Comment(value = "Olusturulma tarihi")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE", nullable = false)
    private Date createDate;
}
