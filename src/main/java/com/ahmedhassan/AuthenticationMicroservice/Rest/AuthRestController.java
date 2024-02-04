package com.ahmedhassan.AuthenticationMicroservice.Rest;


import com.ahmedhassan.AuthenticationMicroservice.Dto.LoginDTO;
import com.ahmedhassan.AuthenticationMicroservice.Dto.UserDTO;
import com.ahmedhassan.AuthenticationMicroservice.Response.AuthenticationResponse;
import com.ahmedhassan.AuthenticationMicroservice.Service.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthRestController {

    private final UserService userService;

    public AuthRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signUp(@RequestBody UserDTO userDTO)
    {

        AuthenticationResponse authRes = userService.addUser(userDTO);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(authRes);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> loginUser(@RequestBody LoginDTO loginDTO){

        return ResponseEntity.ok(userService.login(loginDTO));

    }
    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@RequestParam (name = "token") String token, @RequestParam ("url") String url)
    {
        return ResponseEntity.ok().build();
    }

}
