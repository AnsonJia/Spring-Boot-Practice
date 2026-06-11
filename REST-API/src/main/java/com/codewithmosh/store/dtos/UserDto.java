package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor //needs a constructor for the fields
@Getter// so spring can read the data and create JSON object
public class UserDto {
    //add fields we want to expose to the outside
    private Long id;
    private String name;
    private String email;
}
