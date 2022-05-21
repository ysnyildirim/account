package com.yil.authentication.user.model;

import com.yil.authentication.base.AbstractEntity;
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
@Table(name = "User")
public class User extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "User_Sequence_Generator",
            sequenceName = "Seq_User",
            allocationSize = 1)
    @GeneratedValue(generator = "User_Sequence_Generator")
    @Column(name = "Id")
    private Long id;
    @Column(name = "UserName", nullable = false, unique = true, length = 100)
    private String userName;
    @Column(name = "Password", nullable = false, length = 100)
    private String password;
    @Column(name = "UserTypeId", nullable = false)
    private Long userTypeId;
    @Column(name = "Enabled", nullable = false)
    private Boolean enabled;
    @Column(name = "Locked", nullable = false)
    private Boolean locked;
    @Column(name = "LastPasswordChangeTime")
    private Date lastPasswordChangeTime;
    @Column(name = "Mail", nullable = false)
    private String mail;
    @Column(name = "PersonId")
    private Long personId;
    @Column(name = "CompanyId")
    private Long companyId;
}
