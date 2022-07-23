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
@Table(schema = "GRP",name = "GROUP_ROLE")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupRole implements IEntity {

    @EmbeddedId
    private Pk id;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Pk implements Serializable {
        @Column(name = "GROUP_ID", nullable = false)
        private Long groupId;
        @Column(name = "ROLE_ID", nullable = false)
        private Long roleId;
    }

}
