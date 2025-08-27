package com.rainman.helloaws.entity.mapper;

import com.rainman.helloaws.dto.user.UserCreateDTO;
import com.rainman.helloaws.dto.user.UserResponseDTO;
import com.rainman.helloaws.dto.user.UserUpdateDTO;
import com.rainman.helloaws.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserCreateDTO userDTO);

    User toEntity(UserUpdateDTO userDTO);

    UserResponseDTO toDTO(User user);

    List<UserResponseDTO> toDTOList(List<User> users);
}
