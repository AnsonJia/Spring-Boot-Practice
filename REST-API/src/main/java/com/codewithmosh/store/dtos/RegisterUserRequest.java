package com.codewithmosh.store.dtos;

import lombok.Data;


@Data //combination of @Getter, @Setter, @toString, @toHashCode
public class RegisterUserRequest {
    // we don't need id to create a new user since its auto generated
    private String name;
    private String email;
    private String password; //we put password here for user registration instead of UserDto
}
