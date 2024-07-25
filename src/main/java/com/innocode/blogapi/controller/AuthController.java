package com.innocode.blogapi.controller;

import com.innocode.blogapi.model.request.RegisterReq;
import com.innocode.blogapi.model.dto.UserLoginDto;
import com.innocode.blogapi.model.response.ApiResponse;
import com.innocode.blogapi.model.response.UserResponse;
import com.innocode.blogapi.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AuthController {
    private final UserService authservice;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody RegisterReq req) {
        return new ResponseEntity<>(authservice.registerUser(req), HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@Valid @RequestBody UserLoginDto loginDto) {
        return ResponseEntity.ok(authservice.loginUser(loginDto));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile() {
        ApiResponse<UserResponse> response = authservice.getUserProfile();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
