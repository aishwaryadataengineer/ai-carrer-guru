package com.aelionix.airesumebuilder.dto;

import java.time.LocalDate;


// EducationDTO.java
public record EducationDTO(
        Long eduId,
        String schoolName,
        String degree,
        String fieldOfStudy,
        LocalDate graduationDate
) {}