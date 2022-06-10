package com.yil.account.user.model;

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
public class UserPhoto implements IEntity {
    @Id
    @SequenceGenerator(name = "USER_PHOTO_SEQUENCE_GENERATOR",
            sequenceName = "SEQ_USER_PHOTO",
            allocationSize = 1)
    @GeneratedValue(generator = "USER_PHOTO_SEQUENCE_GENERATOR")
    @Column(name = "ID")
    private Long id;
    @Column(name = "USER_ID", nullable = false)
    private Long userId;
    @Column(name = "DOCUMENT_ID", nullable = false)
    private Long documentId;

}



