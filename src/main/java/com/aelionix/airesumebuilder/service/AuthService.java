package com.aelionix.airesumebuilder.service;

import com.aelionix.airesumebuilder.dto.StandardResponse;
import com.aelionix.airesumebuilder.dto.LoginResponse;
import com.aelionix.airesumebuilder.dto.UserResponseDTO;
import com.aelionix.airesumebuilder.model.GoogleLoginRequest;
import com.aelionix.airesumebuilder.model.LoginRequest;
import com.aelionix.airesumebuilder.model.SignupRequest;
import com.aelionix.airesumebuilder.model.User;
import com.aelionix.airesumebuilder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final CustomUserDetailsService userDetailsService;
    private final GoogleOAuthService googleOAuthService;

    public StandardResponse signup(SignupRequest signupRequest) {
        
        Optional<User> existingUser = userRepository.findByUserName(signupRequest.getUserName());
        if (existingUser.isPresent()) {
            return new StandardResponse("error", 400, "Username is already taken", null);
        }

        // Check if email is already registered
        Optional<User> existingEmailUser = userRepository.findByEmailId(signupRequest.getEmailId());
        if (existingEmailUser.isPresent()) {
            return new StandardResponse("error", 400, "Email is already registered", null);
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

        userRepository.save(user);

        // Build the UserResponseDTO record from the saved user
        UserResponseDTO responseDTO = new UserResponseDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmailId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getTitle(),
                user.getLocation(),
                user.getCreated(),
                user.getUpdated()
        );

        // Return the created user's details in the response.
        return new StandardResponse("success", 201, "User registered successfully", responseDTO);
    }

    public StandardResponse login(LoginRequest loginRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword())
            );
            // Generate JWT token if authentication is successful
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtTokenService.generateToken(userDetails);

            // Fetch user details
            Optional<User> userOptional = userRepository.findByUserName(loginRequest.getUserName());
            if (userOptional.isEmpty()) {
                return new StandardResponse("error", 401, "User not found", null);
            }
            User user = userOptional.get();

            // Create a response payload using the LoginResponse record
            LoginResponse data = new LoginResponse(
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

            log.info("Login successful for user: {}", user.getUserName());
            return new StandardResponse("success", 200, "Login successful", data);

        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return new StandardResponse("error", 401, "Invalid credentials", null);
        }
    }

    public StandardResponse googleLogin(GoogleLoginRequest googleLoginRequest) {
        try {
            // Process Google login and get/create the corresponding user
            User user = googleOAuthService.processGoogleUser(googleLoginRequest.getToken());

            // Generate JWT token
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            String token = jwtTokenService.generateToken(userDetails);

            // Build the response payload using the LoginResponse record
            LoginResponse data = new LoginResponse(
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

            log.info("Google login successful for user: {}", user.getUserName());
            return new StandardResponse("success", 200, "Google login successful", data);

        } catch (Exception e) {
            log.error("Google login failed: {}", e.getMessage());
            return new StandardResponse("error", 401, "Google login failed", null);
        }
    }
}
