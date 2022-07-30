package com.yil.account.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGroupDto {
    @NotBlank
    @Length(min = 1, max = 100)
    private String name;
    @Length(min = 1, max = 100)
    private String description;
    private String email;
}
