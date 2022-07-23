package com.yil.account.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    private Integer userTypeId;
    @NotNull
    private Boolean enabled;
    @NotNull
    private Boolean locked;
   //@NotBlank
   //@Email
    private String mail;
    @NotNull
    private Boolean passwordNeedsChanged;
    private Long personId;
}
