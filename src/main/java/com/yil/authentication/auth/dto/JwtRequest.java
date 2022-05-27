package com.yil.authentication.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@NoArgsConstructor
@Data
public class JwtRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    @NotBlank
    @Length(min = 1,max = 100)
    private String username;
    @NotBlank
    @Length(min = 1,max = 100)
    private String password;

}
