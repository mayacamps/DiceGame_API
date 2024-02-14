package com.itacademy.diceGame.service.impl;

import com.itacademy.diceGame.exceptions.UserAlreadyExistsException;
import com.itacademy.diceGame.model.dto.request.SignInRequest;
import com.itacademy.diceGame.model.dto.request.SignUpRequest;
import com.itacademy.diceGame.model.dto.response.JwtAuthenticationResponse;
import com.itacademy.diceGame.model.entity.User;
import com.itacademy.diceGame.model.enums.Role;
import com.itacademy.diceGame.repository.UserRepository;
import com.itacademy.diceGame.service.AuthenticationService;
import com.itacademy.diceGame.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signup(SignUpRequest request) {
        if (request.getEmail().isEmpty() || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("User email and password cannot be null");
        }
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("Email is already registered:" + user.getEmail());
                });
        var user = User.builder().firstName(request.getFirstName()).lastName(request.getLastName())
                .email(request.getEmail()).password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER).build();
        userRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }

    @Override
    public JwtAuthenticationResponse signin(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + request.getEmail()));
        var jwt = jwtService.generateToken(user);
        return JwtAuthenticationResponse.builder().token(jwt).build();
    }
}
