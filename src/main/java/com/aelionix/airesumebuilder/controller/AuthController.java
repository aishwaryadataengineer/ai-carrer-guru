package com.aelionix.airesumebuilder.controller;

import com.aelionix.airesumebuilder.dto.LoginResponse;
import com.aelionix.airesumebuilder.model.LoginRequest;
import com.aelionix.airesumebuilder.model.GoogleLoginRequest;
import com.aelionix.airesumebuilder.model.SignupRequest;
import com.aelionix.airesumebuilder.model.User;
import com.aelionix.airesumebuilder.repository.UserRepository;
import com.aelionix.airesumebuilder.service.CustomUserDetailsService;
import com.aelionix.airesumebuilder.service.GoogleOAuthService;
import com.aelionix.airesumebuilder.service.JwtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // Check if the username is already taken
        Optional<User> existingUser = userRepository.findByUserName(signupRequest.getUserName());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is already taken");
        }

        // Check if email is already registered
        Optional<User> existingEmailUser = Optional.ofNullable(userRepository.findByEmailId(signupRequest.getEmailId()));
        if (existingEmailUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already registered");
        }

        // Create and save new user (password is encoded)
        User user = new User();
        user.setUserName(signupRequest.getUserName());
        user.setEmailId(signupRequest.getEmailId());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setPhone(signupRequest.getPhone());
        user.setTitle(signupRequest.getTitle());
        user.setLocation(signupRequest.getLocation());
        user.setCreated(new Date());
        user.setUpdated(new Date());

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );

            // If authentication is successful, generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenService.generateToken(userDetails);

            // Fetch user details
            Optional<User> userOptional = userRepository.findByUserName(loginRequest.getUserName());

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // Create login response
                LoginResponse loginResponse = new LoginResponse(
                        token,
                        user.getUserId(),
                        user.getUserName(),
                        user.getEmailId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPhone(),
                        user.getTitle(),
                        user.getLocation()
                );

                System.out.println("Login Successful for user: " + user.getUserName());
                return ResponseEntity.ok(loginResponse);
            }

            System.out.println("Login failed: User not found");
            // If user not found (which should not happen after successful authentication)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        } catch (Exception e) {
            // Log the actual error for server-side tracking
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) {
        try {
            // Process Google login
            User user = googleOAuthService.processGoogleUser(googleLoginRequest.getToken());

            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String token = jwtTokenService.generateToken(userDetails);

            // Create login response
            LoginResponse loginResponse = new LoginResponse(
                    token,
                    user.getUserId(),
                    user.getUserName(),
                    user.getEmailId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPhone(),
                    user.getTitle(),
                    user.getLocation()
            );
            System.out.println("Google Login Success"+ResponseEntity.ok(loginResponse));
            return ResponseEntity.ok(loginResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Google login failed");
        }
    }
}