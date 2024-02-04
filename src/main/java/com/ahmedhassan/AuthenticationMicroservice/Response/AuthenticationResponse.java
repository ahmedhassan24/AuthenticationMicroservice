package com.ahmedhassan.AuthenticationMicroservice.Response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    String message;
    Boolean status;
    String email;
    String expiresIn;
}
