package com.ahmedhassan.AuthenticationMicroservice.Service;


import com.ahmedhassan.AuthenticationMicroservice.Dto.LoginDTO;
import com.ahmedhassan.AuthenticationMicroservice.Dto.UserDTO;
import com.ahmedhassan.AuthenticationMicroservice.Entity.Role;
import com.ahmedhassan.AuthenticationMicroservice.Entity.User;
import com.ahmedhassan.AuthenticationMicroservice.Repo.UserRepo;
import com.ahmedhassan.AuthenticationMicroservice.Response.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepo userRepo, JwtService jwtService){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }




    @Transactional
    @Override
    public AuthenticationResponse addUser(UserDTO userDTO) {

        userDTO.setRole(Role.USER);
        if(userRepo.existsByEmail(userDTO.getEmail()))
        {
            return new AuthenticationResponse(null,"Email already exists", false, null,null);
        }
        else {
            User user = new User(
                    userDTO.getUserId(),
                    userDTO.getEmail(),
                    passwordEncoder.encode(userDTO.getPassword()),
                    userDTO.getRole()
            );
            userRepo.save(user);
            var jwtToken = jwtService.generateToken(user);
            String expiresIn = String.valueOf(jwtService.extractExpiration(jwtToken));
            return new AuthenticationResponse(jwtToken, "User with email: " + user.getEmail() + " added!", true, user.getEmail(), expiresIn);
        }
    }

    @Override
    public AuthenticationResponse login(LoginDTO loginDTO) {
        Optional<User> tempUser = userRepo.findByEmail(loginDTO.getEmail());
        if(tempUser.isPresent())
        {
            String pass = loginDTO.getPassword();
            User tempUser2 = tempUser.get(); //get the fields of the optional user.
            String encodedPass = tempUser2.getPassword();
            boolean passwordMatch = passwordEncoder.matches(pass, encodedPass);
            if(passwordMatch){
                Optional<User> user = userRepo.findOneByEmailAndPassword(loginDTO.getEmail(),encodedPass);
                if(user.isPresent())
                {
                    var jwtToken = jwtService.generateToken(user.get());
                    String expiresIn = String.valueOf(jwtService.extractExpiration(jwtToken));
                    return new AuthenticationResponse(jwtToken,"Login Successful", true, user.get().getEmail(),expiresIn );
                }
                else {
                    return new AuthenticationResponse(null, "Login Failed", false , user.get().getEmail(),null);
                }
            }
            else
            {
                return new AuthenticationResponse(null, "Password does not match", false, tempUser2.getEmail(),null);
            }

        }
        else {
            return new AuthenticationResponse(null, "Login Failed, email does not exist", false, tempUser.get().getEmail(),null);
        }
    }
}
