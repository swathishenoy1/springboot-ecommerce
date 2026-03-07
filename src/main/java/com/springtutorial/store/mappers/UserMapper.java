package com.springtutorial.store.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.springtutorial.store.dtos.RegisterUserRequest;
import com.springtutorial.store.dtos.UpdateUserRequest;
import com.springtutorial.store.dtos.UserDto;
import com.springtutorial.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest request);
    void updateUser(UpdateUserRequest updateUserRequest, @MappingTarget User user);
    
}
