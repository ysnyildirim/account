package com.yil.account.user.model;

import com.yil.account.base.AbstractEntity;
import com.yil.account.base.IEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "USER_PHOTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoto extends AbstractEntity {
    @Id
    @SequenceGenerator(name = "USER_PHOTO_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_PHOTO_ID",
            allocationSize = 1)
    @GeneratedValue(generator = "USER_PHOTO_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "NAME", nullable = false, length = 100)
    private String name;
    @Column(name = "EXTENSION", nullable = false, length = 10)
    private String extension;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENT", nullable = false)
    private Byte[] content;
}



