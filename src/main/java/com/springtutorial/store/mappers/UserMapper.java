package com.springtutorial.store.mappers;

import org.mapstruct.Mapper;

import com.springtutorial.store.dtos.UserDto;
import com.springtutorial.store.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    
}
