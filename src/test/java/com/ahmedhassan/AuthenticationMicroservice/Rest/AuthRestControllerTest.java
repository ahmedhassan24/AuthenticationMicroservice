package com.ahmedhassan.AuthenticationMicroservice.Rest;

import com.ahmedhassan.AuthenticationMicroservice.Dto.UserDTO;
import com.ahmedhassan.AuthenticationMicroservice.Entity.Role;
import com.ahmedhassan.AuthenticationMicroservice.Entity.User;
import com.ahmedhassan.AuthenticationMicroservice.Repo.UserRepo;
import com.ahmedhassan.AuthenticationMicroservice.Response.AuthenticationResponse;
import com.ahmedhassan.AuthenticationMicroservice.Service.JwtService;
import com.ahmedhassan.AuthenticationMicroservice.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthRestController.class)
@AutoConfigureMockMvc(addFilters = false )
class AuthRestControllerTest {
    @Autowired
    private AuthRestController authrestcontorller;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Mock
    private UserRepo userRepo;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtService jwtService;


    @BeforeEach
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    @Test
    public void testSignUp() throws Exception {
        // Create a sample user DTO for testing
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("testuser");
        userDTO.setPassword("testpassword");

        // Convert the userDTO object to JSON string
        String userJson = objectMapper.writeValueAsString(userDTO);
        AuthenticationResponse mockResponse = getMockResponse();
        String resJson = objectMapper.writeValueAsString(mockResponse);
        given(userService.addUser(Mockito.any(UserDTO.class))).willReturn(mockResponse);

        // Perform the POST request to the /signup endpoint
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                        .andDo(print())
                .andExpect(jsonPath("$").isNotEmpty())
                        .andExpect(jsonPath("$.email").value(mockResponse.getEmail())).andReturn();

        //
//        MockHttpServletResponse mockHttpServletResponse = mvcResult.getResponse();
//        String result = mvcResult.toString();
//        System.out.println(result);
//        AuthenticationResponse authMockResponse = objectMapper.readValue(mockHttpServletResponse.get(), AuthenticationResponse.class);
//            System.out.println(authMockResponse);
            System.out.println(mockResponse.getEmail());
//                .andExpect()
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email",is(mockResponse.getEmail())))
//                // Add additional assertions if needed
//                .andExpect(MockMvcResultMatchers.jsonPath("$.token").exists());
    }

    public AuthenticationResponse getMockResponse(){
        var user = User.builder()
                .email("testuser")
                .password("testpassword")
                .role(Role.USER)
                .build();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .email(user.getEmail())
                .expiresIn("Sat Feb 03 21:37:59 EET 2024")
                .message("User with email: testuser added!")
                .build();
    }
}
