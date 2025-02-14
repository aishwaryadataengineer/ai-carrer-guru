package com.aelionix.airesumebuilder.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Table(name = "experiences")
@Entity
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String location;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ElementCollection
    @CollectionTable(name = "experience_descriptions", joinColumns = @JoinColumn(name = "experience_id"))
    @Column(name = "description")
    private List<String> description;
}
