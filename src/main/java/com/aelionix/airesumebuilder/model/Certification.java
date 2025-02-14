package com.aelionix.airesumebuilder.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cert_id", nullable = false)
    private Long certId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Links certification to a user

    @Column(nullable = false)
    private String certificationName;

    @Column(nullable = false)
    private String issuingOrganization;  // Example: Microsoft, Coursera, AWS

    @Column(nullable = false)
    private LocalDate issueDate;

    private LocalDate expiryDate;  // Some certifications have expiry dates (nullable)

    private String certificationUrl;  // Optional: Link to certification if available
}
