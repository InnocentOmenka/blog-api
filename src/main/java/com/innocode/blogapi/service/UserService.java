package com.innocode.blogapi.service;

import com.innocode.blogapi.model.response.ApiResponse;
import com.innocode.blogapi.model.request.RegisterReq;
import com.innocode.blogapi.model.entity.User;
import com.innocode.blogapi.model.dto.UserLoginDto;
import com.innocode.blogapi.model.response.UserResponse;
import com.innocode.blogapi.repository.UserRepository;
import com.innocode.blogapi.security.JwtUtils;
import com.innocode.blogapi.security.UserDetailsServiceImpl;
import com.innocode.blogapi.exception.BadCredentialsException;
import com.innocode.blogapi.exception.UserAlreadyExistException;
import com.innocode.blogapi.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtil;
    private final AuthenticationManager auth;
    private final UserDetailsServiceImpl userDetailsService;

    public ApiResponse<Object> registerUser(RegisterReq req) {
        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new UserAlreadyExistException("Email already exists");
        }
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new UserAlreadyExistException("Username already exists");
        }

        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new BadCredentialsException("Passwords do not match");
        }
        User user = new User();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(encoder.encode(req.getPassword()));
        userRepository.save(user);
        return new ApiResponse<>("success", "User created successfully", true);
    }

    public ApiResponse<Object> loginUser(UserLoginDto loginDto) {
        String email = loginDto.getEmail();
        String password = loginDto.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email '%s' not found", email)));

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Wrong Password");
        }

        Authentication authentication = auth.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);

        return new ApiResponse<>("success", "User logged in successfully", token);
    }
    public ApiResponse<UserResponse> getUserProfile() {
        String email = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with email '%s' not found", email)));

        UserResponse userResponse = UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return new ApiResponse<>("success", "User profile fetched successfully", userResponse);
    }
}
