package com.ahmedhassan.AuthenticationMicroservice.Service;

import com.ahmedhassan.AuthenticationMicroservice.Dto.LoginDTO;
import com.ahmedhassan.AuthenticationMicroservice.Dto.UserDTO;
import com.ahmedhassan.AuthenticationMicroservice.Entity.Role;
import com.ahmedhassan.AuthenticationMicroservice.Repo.UserRepo;
import com.ahmedhassan.AuthenticationMicroservice.Response.AuthenticationResponse;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext
public class UserServiceTest {

    @Resource
    private UserRepo userRepo;
    @MockBean
    private JwtService jwtService;

   private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;


    @BeforeEach
    public void init(){
        userService = new UserServiceImpl( passwordEncoder,  userRepo,  jwtService);
    }

    @Test
    void addUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole(Role.USER);
        userDTO.setUserId(1);
        when(jwtService.generateToken(Mockito.any())).thenReturn("token");
        AuthenticationResponse authenticationResponse = userService.addUser(userDTO);
        System.out.println(authenticationResponse);
        assertEquals(authenticationResponse.getEmail(), userDTO.getEmail());
        assertEquals(authenticationResponse.getToken(), "token");
        assertTrue(userRepo.existsByEmail(userDTO.getEmail()));
    }

    @Test
    void login_success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole(Role.USER);
        userDTO.setUserId(1);
        when(passwordEncoder.encode("pass123")).thenReturn("pass123");
        when(passwordEncoder.matches(Mockito.any(),Mockito.any())).thenReturn(true);
         userService.addUser(userDTO);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword(passwordEncoder.encode("pass123"));
        AuthenticationResponse authenticationResponse1 = userService.login(loginDTO);
        assertEquals(authenticationResponse1.getEmail(), loginDTO.getEmail());
        assertTrue(userRepo.existsByEmail(loginDTO.getEmail()));
        assertEquals(authenticationResponse1.getMessage(),"Login Successful");

}

   @Test
   void login_fail() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole(Role.USER);
        userDTO.setUserId(1);
        when(passwordEncoder.encode("pass123")).thenReturn("pass1233");
        when(passwordEncoder.matches(Mockito.any(),Mockito.any())).thenReturn(false);
        userService.addUser(userDTO);
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail("test@example.com");
        loginDTO.setPassword(passwordEncoder.encode("pass123"));
        AuthenticationResponse authenticationResponse1 = userService.login(loginDTO);
       System.out.println(authenticationResponse1);
        assertEquals(authenticationResponse1.getEmail(), loginDTO.getEmail());
        assertTrue(userRepo.existsByEmail(loginDTO.getEmail()));
        assertEquals(authenticationResponse1.getMessage(),"Password does not match");

    }
}