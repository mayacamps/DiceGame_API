package com.itacademy.diceGame.service;

import com.itacademy.diceGame.model.dto.request.SignInRequest;
import com.itacademy.diceGame.model.dto.request.SignUpRequest;
import com.itacademy.diceGame.model.dto.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}
