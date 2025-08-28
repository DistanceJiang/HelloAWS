package com.rainman.helloaws.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO extends BaseUserDTO {
    // no id
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
