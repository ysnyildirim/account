package com.yil.account.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {
    @NotBlank
    @Length(min = 1, max = 100)
    private String currentPassword;
    @NotBlank
    @Length(min = 1, max = 100)
    private String newPassword;
}
