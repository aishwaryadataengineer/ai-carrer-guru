package com.aelionix.airesumebuilder.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "education")
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Change this if using PostgreSQL
    @Column(name = "edu_id", nullable = false)
    private Long eduId;

    @Column(nullable = false)
    private String schoolName;

    @Column(nullable = false)
    private String degree;  // Represents Bachelor's, Master's, etc.

    @Column(nullable = false)
    private String fieldOfStudy; // Example: Computer Science, Data Engineering

    @Column(nullable = false)
    private LocalDate graduationDate; // Stores the award/graduation date properly

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
