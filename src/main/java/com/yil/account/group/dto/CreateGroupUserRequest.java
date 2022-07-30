package com.yil.account.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupUserRequest {
    @NotNull
    private Long userId;
    @NotNull
    private Integer groupUserTypeId;
}
