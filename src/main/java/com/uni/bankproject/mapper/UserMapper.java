package com.uni.bankproject.mapper;


import com.uni.bankproject.dto.UserDTO;
import com.uni.bankproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(source = "userID", target = "userID")
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")

    UserDTO mapToDTO(User user);
    @Mapping(source = "userID", target = "userID")
    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "address", target = "address")

    User mapToUser(UserDTO userDTO);
}
