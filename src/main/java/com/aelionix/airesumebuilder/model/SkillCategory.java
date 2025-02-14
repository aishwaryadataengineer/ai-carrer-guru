package com.aelionix.airesumebuilder.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "skill_categories")
public class SkillCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(unique = true, nullable = false)
    private String categoryName;  // Example: "Programming Languages", "DevOps Tools"
}
