package com.example.Mini_Banking.controller;

import com.example.Mini_Banking.payload.dto.LoginDTO;
import com.example.Mini_Banking.payload.dto.RegisterDTO;
import com.example.Mini_Banking.payload.response.AuthResponse;
import com.example.Mini_Banking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO loginDTO){
        return ResponseEntity.ok(
                authService.login(loginDTO)
        );
    }

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterDTO registerDTO){
        return ResponseEntity.ok(
                authService.register(registerDTO)
        );
    }
}
