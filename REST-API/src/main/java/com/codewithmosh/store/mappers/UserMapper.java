package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

//MapStruct will automatically implement this interface at runtime (target/generated-sources/annotations folder)
@Mapper(componentModel = "spring") //tells spring to manage the lifecycle of this mapper and inject it where needed
public interface UserMapper {
    //customize mapping code and valid string java expression java(java expression)
    //@Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")//find local date time now
    UserDto toDto(User user);//method to map user to userDto for getting data

    User toEntity(RegisterUserRequest request);//method to map post request to user entity for posting data
}
