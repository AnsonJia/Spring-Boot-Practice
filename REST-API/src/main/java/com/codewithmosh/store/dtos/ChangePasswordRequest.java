package com.codewithmosh.store.dtos;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    //separate dto for handling change password requests
    private String oldPassword;
    private String newPassword;
}
