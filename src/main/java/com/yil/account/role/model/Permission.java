package com.yil.account.role.model;

import com.yil.account.base.IEntity;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "RL", name = "PERMISSION")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Permission implements IEntity {
    @Id
    @SequenceGenerator(schema = "RL",
            name = "PERMISSION_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_PERMISSION_ID")
    @GeneratedValue(generator = "PERMISSION_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 1000)
    private String name;
    @Column(name = "DESCRIPTION", length = 4000)
    private String description;
    @Comment("Yetki atanabilir mi? Sistem yetkileri uygulama için gerekli yetkilerdir. Bu yetkilere sadece yöneticiler erişebilir.")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @ColumnDefault(value = "0")
    @Column(name = "ASSIGNABLE", nullable = false)
    private boolean assignable;
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
