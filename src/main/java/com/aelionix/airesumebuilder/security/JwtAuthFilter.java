package com.aelionix.airesumebuilder.security;

import com.aelionix.airesumebuilder.service.CustomUserDetailsService;
import com.aelionix.airesumebuilder.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        // Get the Authorization header
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // Check if Authorization header exists and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extract JWT token
            jwt = authorizationHeader.substring(7);
            
            try {
                // Extract username from JWT token
                username = jwtTokenService.extractUsername(jwt);
            } catch (Exception e) {
                // Log the exception or handle token extraction errors
                logger.error("Error extracting username from token", e);
            }
        }

        // If username is extracted and no authentication is currently set
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the token
            if (jwtTokenService.validateToken(jwt, userDetails)) {
                // Create authentication token
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                    );
                
                // Set details from the web request
                usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}