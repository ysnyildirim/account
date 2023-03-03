package com.yil.account.role.model;

import com.yil.account.base.IEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(schema = "RL", name = "ROLE_PERMISSION")
public class RolePermission implements IEntity {
    @EmbeddedId
    private Pk id;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "ROLE_ID", nullable = false)
        private Integer roleId;
        @Column(name = "PERMISSION_ID", nullable = false)
        private Integer permissionId;
    }
}
