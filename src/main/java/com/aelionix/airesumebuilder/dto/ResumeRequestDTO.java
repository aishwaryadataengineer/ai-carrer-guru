package com.aelionix.airesumebuilder.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public record ResumeRequestDTO(
        String name,
        String title,
        String email,
        String phone,
        String linkedin,
        String github,
        String location,
        String summary,
        List<ExperienceDTO> experiences,
        Map<String, List<String>> skills,
        List<CertificationDTO> certifications, // Updated field type
        List<EducationDTO> education
) {}
