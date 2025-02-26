package com.aelionix.airesumebuilder.controller;

import com.aelionix.airesumebuilder.dto.ExperienceDTO;
import com.aelionix.airesumebuilder.dto.StandardResponse;
import com.aelionix.airesumebuilder.model.CandidateResume;
import com.aelionix.airesumebuilder.model.Experience;
import com.aelionix.airesumebuilder.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ExperienceController {

    private final ExperienceRepository experienceRepository;

    @PutMapping("/{experienceId}")
    public ResponseEntity<StandardResponse> updateExperience(
            @PathVariable Long experienceId,
            @RequestBody ExperienceDTO experienceDTO) {

        // Fetch the existing experience record.
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Experience not found"));

        // Get the candidate resume associated with the experience.
        CandidateResume candidateResume = experience.getCandidateResume();

        // Check if another experience for the same candidate resume already has this company.
        Optional<Experience> duplicate = experienceRepository.findByCandidateResumeAndCompany(candidateResume, experienceDTO.company());
        if (duplicate.isPresent() && !duplicate.get().getExperienceId().equals(experienceId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company name already exists for this resume");
        }

        // Update the experience fields.
        experience.setCompany(experienceDTO.company());
        experience.setTitle(experienceDTO.title());
        experience.setLocation(experienceDTO.location());
        experience.setStartDate(experienceDTO.startDate());
        experience.setEndDate(experienceDTO.endDate());
        experience.setDescription(experienceDTO.description());

        // Save the updated experience.
        Experience updatedExperience = experienceRepository.save(experience);

        // Return a standard response.
        StandardResponse response = new StandardResponse("success", 200, "Experience updated successfully", updatedExperience);
        return ResponseEntity.ok(response);
    }
}
