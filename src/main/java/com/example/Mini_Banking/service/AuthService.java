package com.example.Mini_Banking.service;

import com.example.Mini_Banking.constant.UserRole;
import com.example.Mini_Banking.entity.AppUser;
import com.example.Mini_Banking.payload.dto.LoginDTO;
import com.example.Mini_Banking.payload.dto.RegisterDTO;
import com.example.Mini_Banking.payload.response.AuthResponse;
import com.example.Mini_Banking.repository.AppUserRepository;
import com.example.Mini_Banking.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginDTO loginRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        AppUser user = appUserRepository.findUserByEmail(loginRequest.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, "Login successfully");
    }

    public AuthResponse register(RegisterDTO request){
        if (appUserRepository.findUserByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }

        var user = AppUser.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(UserRole.USER.name())
                .build();

        appUserRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token, "Successful Register");
    }
}
