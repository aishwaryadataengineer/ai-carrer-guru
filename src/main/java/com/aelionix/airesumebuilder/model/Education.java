package com.aelionix.airesumebuilder.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "education")
// In Education.java
@JsonIgnoreProperties("candidateResume")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "edu_id", nullable = false)
    private Long eduId;

    @Column(nullable = false)
    private String schoolName;

    @Column(nullable = false)
    private String degree;

    @Column(nullable = false)
    private String fieldOfStudy;

    @Column(nullable = false)
    private LocalDate graduationDate;

    // Add this field to map back to CandidateResume
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private CandidateResume candidateResume;
}
