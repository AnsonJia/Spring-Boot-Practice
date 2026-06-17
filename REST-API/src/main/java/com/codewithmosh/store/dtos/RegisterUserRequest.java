package com.codewithmosh.store.dtos;

import com.codewithmosh.store.validation.Lowercase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data //combination of @Getter, @Setter, @toString, @toHashCode
public class RegisterUserRequest {
    // we don't need id to create a new user since its auto generated
    @NotBlank(message = "Name is required") // makes sure name isn't empty string or whitespace "", "   "
    @Size(max = 255, message = "Name must be less than 255 characters") // makes sure name isn't too long
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid") // makes sure email is valid/in correct format
    @Lowercase(message = "Email must be lowercase") // custom validation to make sure email is in lowercase
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 25, message = "Password must be between 6 and 25 characters long")//make sure password is between 6-25 char long
    private String password; //we put password here for user registration instead of UserDto
}
