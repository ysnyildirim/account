package com.yil.account.role.model;

import com.yil.account.base.IEntity;
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
@Table(schema = "RL",
        name = "ROLE",
        uniqueConstraints = @UniqueConstraint(name = "UC_ROLE_NAME", columnNames = {"NAME"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements IEntity {
    @Id
    @SequenceGenerator(schema = "RL",
            name = "ROLE_ID_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_ROLE_ID")
    @GeneratedValue(generator = "ROLE_ID_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME", nullable = false, length = 1000)
    private String name;
    @Column(name = "DESCRIPTION", length = 4000)
    private String description;
    @Comment(value = "Rol atanabilir mi?")
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
