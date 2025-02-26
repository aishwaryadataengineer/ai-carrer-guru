package com.aelionix.airesumebuilder.dto;

import java.time.LocalDate;

public record CertificationDTO(
    String certificationName,
    String issuingOrganization,
    LocalDate issueDate,
    LocalDate expiryDate,
    String certificationUrl
) {}