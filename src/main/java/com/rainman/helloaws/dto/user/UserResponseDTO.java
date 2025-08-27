package com.rainman.helloaws.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO extends BaseUserDTO {
    private Long id;
    private LocalDateTime createdAt;
}
