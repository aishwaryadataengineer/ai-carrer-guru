package com.aelionix.airesumebuilder.service;

import com.aelionix.airesumebuilder.dto.ResumeRequestDTO;
import com.aelionix.airesumebuilder.dto.ResumeResponseDTO;
import com.aelionix.airesumebuilder.model.CandidateResume;

import java.util.List;

public interface ResumeService {
    ResumeResponseDTO saveResume(ResumeRequestDTO resumeRequest);
    ResumeResponseDTO updateResume(Long resumeId, ResumeRequestDTO resumeRequest);
    ResumeResponseDTO getResumeById(Long resumeId);
    List<ResumeResponseDTO> findResumesByUserCriteria(String userName, String email, String title);
    List<ResumeResponseDTO> getResumesByUserId(Long userId);
}
