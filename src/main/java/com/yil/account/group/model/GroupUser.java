package com.yil.account.group.model;

import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "GRP", name = "GROUP_USER")
public class GroupUser implements IEntity {

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
        @Column(name = "GROUP_ID", nullable = false)
        private Long groupId;
        @Column(name = "GROUP_USER_TYPE_ID", nullable = false)
        private Integer groupUserTypeId;
    }
}
