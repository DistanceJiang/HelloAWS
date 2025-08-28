package com.rainman.helloaws.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO extends BaseUserDTO {
    // no id
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
