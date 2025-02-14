package com.aelionix.airesumebuilder.service;

import com.aelionix.airesumebuilder.model.User;
import com.aelionix.airesumebuilder.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class GoogleOAuthService {

    @Value("${google.client.id}")
    private String googleClientId;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenService jwtTokenService;

    private final NetHttpTransport transport = new NetHttpTransport();
    private final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    public User processGoogleUser(String idTokenString) throws GeneralSecurityException, IOException {
        // Verify Google ID Token
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken idToken = verifier.verify(idTokenString);
        if (idToken == null) {
            throw new IllegalArgumentException("Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = idToken.getPayload();

        // Extract user details from Google token
        String email = payload.getEmail();
        String firstName = (String) payload.get("given_name");
        String lastName = (String) payload.get("family_name");

        // Check if user already exists
        Optional<User> existingUser = Optional.ofNullable(userRepository.findByEmailId(email));

        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // Create new user if not exists
        User newUser = new User();
        newUser.setEmailId(email);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        
        // Generate a unique username
        String username = generateUniqueUsername(email);
        newUser.setUserName(username);

        // Generate a random password for Google OAuth users
        String randomPassword = UUID.randomUUID().toString();
        newUser.setPassword(passwordEncoder.encode(randomPassword));

        newUser.setCreated(new Date());
        newUser.setUpdated(new Date());

        // Save the new user
        return userRepository.save(newUser);
    }

    private String generateUniqueUsername(String email) {
        String baseUsername = email.split("@")[0];
        String username = baseUsername;
        int counter = 1;

        while (userRepository.findByUserName(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }
}