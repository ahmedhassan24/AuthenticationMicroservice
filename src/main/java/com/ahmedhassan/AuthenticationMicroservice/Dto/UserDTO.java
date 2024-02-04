package com.ahmedhassan.AuthenticationMicroservice.Dto;

import com.ahmedhassan.AuthenticationMicroservice.Entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    private int userId;
    private String email;
    private String password;
    private Role role;

    public UserDTO() {

    }

}
