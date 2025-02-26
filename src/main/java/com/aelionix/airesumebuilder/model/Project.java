package com.aelionix.airesumebuilder.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    // Link the project to a specific candidate resume
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")  // foreign key column in the projects table
    private CandidateResume candidateResume;

    // Optionally, you can still maintain a link to the user directly
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String projectName;

    @Column(length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologiesUsed;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    private String projectUrl;
}
