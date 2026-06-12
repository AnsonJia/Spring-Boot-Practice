package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;

//MapStruct will automatically implement this interface at runtime (target/generated-sources/annotations folder)
@Mapper(componentModel = "spring") //tells spring to manage the lifecycle of this mapper and inject it where needed
public interface UserMapper {
    UserDto toDto(User user);//method to map user to userDto
}
