package com.innocode.blogapi.service;

import com.innocode.blogapi.exception.BadCredentialsException;
import com.innocode.blogapi.exception.UserAlreadyExistException;
import com.innocode.blogapi.exception.UserNotFoundException;
import com.innocode.blogapi.model.dto.UserLoginDto;
import com.innocode.blogapi.model.entity.User;
import com.innocode.blogapi.model.request.RegisterReq;
import com.innocode.blogapi.model.response.ApiResponse;
import com.innocode.blogapi.model.response.LoginResponse;
import com.innocode.blogapi.repository.UserRepository;
import com.innocode.blogapi.security.JwtUtils;
import com.innocode.blogapi.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        RegisterReq req = new RegisterReq();
        req.setEmail("test@example.com");
        req.setUsername("testuser");
        req.setPassword("password");
        req.setConfirmPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(new User()));

        Exception exception = assertThrows(UserAlreadyExistException.class, () -> {
            userService.registerUser(req);
        });

        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void testLoginUser_UserNotFound() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("password");

        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () -> {
            userService.loginUser(loginDto);
        });

        assertEquals("User with email 'test@example.com' not found", exception.getMessage());
    }

    @Test
    void testLoginUser_WrongPassword() {
        UserLoginDto loginDto = new UserLoginDto();
        loginDto.setEmail("test@example.com");
        loginDto.setPassword("wrongpassword");

        User user = new User();
        user.setPassword("encodedpassword");

        when(userRepository.findByEmail(anyString())).thenReturn(java.util.Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Exception exception = assertThrows(BadCredentialsException.class, () -> {
            userService.loginUser(loginDto);
        });

        assertEquals("Wrong Password", exception.getMessage());
    }

}
