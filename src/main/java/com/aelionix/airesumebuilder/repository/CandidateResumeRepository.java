package com.aelionix.airesumebuilder.repository;

import com.aelionix.airesumebuilder.model.CandidateResume;
import com.aelionix.airesumebuilder.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandidateResumeRepository extends JpaRepository<CandidateResume, Long> {
    List<CandidateResume> findByUserAndTitle(User user, String title);
    List<CandidateResume> findByTitleContainingIgnoreCase(String title);
    List<CandidateResume> findByUser(User user);
}
