package com.codewithmosh.store.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor //needs a constructor for the fields
@Getter// so spring can read the data and create JSON object
public class UserDto {
    //add fields we want to expose to the outside
    //@JsonIgnore// to ignore the id field and not expose it to clients (if we want to hide it)
    //@JsonProperty("user_id")// to change the name of the field in the JSON response (if we want to rename it)
    private Long id;
    private String name;
    private String email;

    //@JsonInclude(JsonInclude.Include.NON_NULL)//only include field in response if not null (if we want to conditionally include it)
    //private String phoneNumber; //phone num col doesn't exist in db, so it will be null

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")//change the format of the date, time, numbers in the JSON response
    //private LocalDateTime createdAt;

    //@JsonIgnore// applied both at serialization and de-serialization so cannot read JSON request to put in dto either
    //private String password; //will not be in dto, make a new dto for registering user that includes password and then map to this dto without the password field
}
