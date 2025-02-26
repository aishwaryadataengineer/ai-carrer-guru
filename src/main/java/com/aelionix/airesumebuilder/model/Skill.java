package com.aelionix.airesumebuilder.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id", nullable = false)
    private Long skillId;

    @Column(nullable = false)
    private String skillName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private SkillCategory skillsCategory;

    @Column(length = 500)
    private String description;

    // Optional: a field to indicate proficiency level (e.g., Beginner, Intermediate, Advanced)
    private String proficiency;
}
