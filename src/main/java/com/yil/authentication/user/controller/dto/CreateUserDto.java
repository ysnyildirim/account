package com.yil.authentication.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    @NotBlank
    @Length(min = 1, max = 100)
    private String userName;
    @NotBlank
    @Length(min = 1, max = 100)
    private String password;
    @NotNull
    @Min(value = 1)
    private Long userTypeId;
    @NotNull
    private Boolean enabled;
    @NotNull
    private Boolean locked;
    @NotBlank
    @Email
    private String mail;
}
