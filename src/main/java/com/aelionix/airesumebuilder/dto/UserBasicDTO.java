package com.aelionix.airesumebuilder.dto;

public record UserBasicDTO(
    Long userId,
    String userName,
    String emailId,
    String firstName,
    String lastName,
    String phone,
    String title,
    String location
) {}