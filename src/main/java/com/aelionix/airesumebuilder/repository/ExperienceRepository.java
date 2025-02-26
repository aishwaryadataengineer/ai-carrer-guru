package com.aelionix.airesumebuilder.repository;

import com.aelionix.airesumebuilder.model.CandidateResume;
import com.aelionix.airesumebuilder.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    Optional<Experience> findByCandidateResumeAndCompany(CandidateResume candidateResume, String company);
}
