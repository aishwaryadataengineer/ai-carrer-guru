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

    // Link to the candidate's user account (if needed)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // New field to link this certification to a specific resume
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private CandidateResume candidateResume;

    @Column(nullable = false)
    private String certificationName;

    @Column(nullable = false)
    private String issuingOrganization;

    @Column(nullable = false)
    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String certificationUrl;
}
