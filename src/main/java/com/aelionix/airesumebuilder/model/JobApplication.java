package com.aelionix.airesumebuilder.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "job_applications")
@EntityListeners(AuditingEntityListener.class)
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    // The candidate who applied for the job
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Optional: Reference to the resume metadata used for this application
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private CandidateResume candidateResume;

    // Company and position details
    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String jobTitle;

    // URL linking to the job description or posting
    private String jobDescriptionUrl;

    // Date when the application was submitted
    @Column(nullable = false)
    private LocalDate appliedDate;

    // Status of the application: APPLIED, UNDER_REVIEW, INTERVIEW_SCHEDULED, OFFERED, REJECTED, etc.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    // Optional additional details or notes about the application process
    @Column(length = 2000)
    private String notes;

    // Optional milestone dates during the application process
    private LocalDate interviewDate;
    private LocalDate offerDate;

    // Reasons for rejection if applicable
    @Column(length = 1000)
    private String rejectionReason;

//     Option 1: URL to externally stored file (if using external storage)
     @Column(length = 2048)
     private String resumeFileUrl;

    // Option 2: Physical copy stored as a BLOB in the database
    @Lob
    @Column(name = "resume_file", columnDefinition = "BLOB")
    private byte[] resumeFile;

    // Audit fields for tracking record creation and updates
    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;
}
