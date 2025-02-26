package com.aelionix.airesumebuilder.dto;

import java.time.LocalDate;
import java.util.List;

public record ExperienceDTO(
        Long experienceId,
        String company,
        String title,
        String location,
        LocalDate startDate,
        LocalDate endDate,
        List<String> description
) {}