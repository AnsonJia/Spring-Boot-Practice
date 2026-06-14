package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class UpdateUserRequest {
    //we are only updating basic account info, not password (handle them separately)
    //we also don't want to use the UserDto in case we update the fields in the future that might not be suitable for update process
    public String name;
    public String email;
}
