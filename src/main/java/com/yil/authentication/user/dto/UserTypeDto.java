package com.yil.authentication.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTypeDto {
    private Long id;
    private String name;
    private Boolean realPerson;
    private Boolean enabled;
}
