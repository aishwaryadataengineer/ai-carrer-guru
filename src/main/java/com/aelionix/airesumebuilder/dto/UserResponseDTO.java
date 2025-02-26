package com.aelionix.airesumebuilder.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long userId,
        String userName,
        String emailId,
        String firstName,
        String lastName,
        String phone,
        String title,
        String location,
        LocalDateTime created,
        LocalDateTime updated
) {}
