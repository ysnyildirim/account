/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */
package com.yil.account.user.model;

import com.yil.account.base.IEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Table(schema = "USR", name = "USER_ROLE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements IEntity {
    @EmbeddedId
    private Pk id;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "USER_ID", nullable = false)
        private Long userId;
        @Column(name = "ROLE_ID", nullable = false)
        private Integer roleId;
    }
}