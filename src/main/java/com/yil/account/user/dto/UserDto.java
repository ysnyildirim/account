package com.yil.account.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yasin.yildirim on 3.05.2022.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String userName;
    private Boolean enabled;
    private Boolean locked;
    private Date lastPasswordChangeDate;
    private Integer userTypeId;
    private String mail;
    private Long personId;
    private Boolean passwordNeedsChanged;
}
