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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Links project to a user

    @Column(nullable = false)
    private String projectName;

    @Column(length = 1000)
    private String description;  // Detailed project summary

    @ElementCollection
    @CollectionTable(name = "project_technologies", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name = "technology")
    private List<String> technologiesUsed;  // List of technologies used in the project

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;  // Nullable for ongoing projects

    private String projectUrl;  // Optional: GitHub or live demo link
}
