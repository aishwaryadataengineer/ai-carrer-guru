package com.aelionix.airesumebuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Long userId;
    private String userName;
    private String emailId;
    private String firstName;
    private String lastName;
    private String phone;
    private String title;
    private String location;
}