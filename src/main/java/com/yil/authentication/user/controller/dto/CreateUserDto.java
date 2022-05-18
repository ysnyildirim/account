package com.yil.authentication.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    @NotBlank
    @Size(min = 1, max = 100)
    private String userName;
    @NotBlank
    @Size(min = 1, max = 100)
    private String password;
    @NotNull
    @Min(value = 1)
    private Long userTypeId;
    @NotNull
    private Boolean enabled;
}
