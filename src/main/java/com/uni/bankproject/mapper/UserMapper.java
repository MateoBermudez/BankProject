package com.uni.bankproject.mapper;


import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {

    UserDTO mapToDTO(User user);

    User mapToUser(UserDTO userDTO);
}
