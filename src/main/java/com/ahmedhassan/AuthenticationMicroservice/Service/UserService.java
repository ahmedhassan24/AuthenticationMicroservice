package com.ahmedhassan.AuthenticationMicroservice.Service;

import com.ahmedhassan.AuthenticationMicroservice.Dto.LoginDTO;
import com.ahmedhassan.AuthenticationMicroservice.Dto.UserDTO;
import com.ahmedhassan.AuthenticationMicroservice.Response.AuthenticationResponse;

public interface UserService {
    AuthenticationResponse addUser(UserDTO userDTO);

    AuthenticationResponse login(LoginDTO loginDTO);
}
