package com.yil.account.role.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleDto {
    @NotBlank
    @Length(min = 1, max = 100)
    private String name;
    @NotBlank
    @Length(min = 1, max = 4000)
    private String description;
    @NotNull
    private Boolean assignable;
    @NotNull
    private Boolean inheritable;
}
