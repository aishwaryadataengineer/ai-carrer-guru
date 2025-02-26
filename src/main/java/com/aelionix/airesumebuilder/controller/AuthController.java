package com.aelionix.airesumebuilder.controller;

import com.aelionix.airesumebuilder.dto.StandardResponse;
import com.aelionix.airesumebuilder.model.GoogleLoginRequest;
import com.aelionix.airesumebuilder.model.LoginRequest;
import com.aelionix.airesumebuilder.model.SignupRequest;
import com.aelionix.airesumebuilder.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<StandardResponse> signup(@RequestBody SignupRequest signupRequest) {
        StandardResponse response = authService.signup(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.code()));
    }

    @PostMapping("/login")
    public ResponseEntity<StandardResponse> login(@RequestBody LoginRequest loginRequest) {
        StandardResponse response = authService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.code()));
    }

    @PostMapping("/google-login")
    public ResponseEntity<StandardResponse> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) {
        StandardResponse response = authService.googleLogin(googleLoginRequest);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.code()));
    }
}
