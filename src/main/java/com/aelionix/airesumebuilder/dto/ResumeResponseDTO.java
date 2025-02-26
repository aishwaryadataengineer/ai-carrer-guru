package com.aelionix.airesumebuilder.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Update your ResumeResponseDTO.java to include all fields
public record ResumeResponseDTO(
        Long resumeId,
        UserBasicDTO user,
        String title,
        String summary,
        List<ExperienceDTO> experiences,
        List<EducationDTO> educations,
        List<CertificationDTO> certifications,
        Map<String, List<String>> skills
) {}