package com.aelionix.airesumebuilder.controller;

import com.aelionix.airesumebuilder.dto.ResumeRequestDTO;
import com.aelionix.airesumebuilder.dto.ResumeResponseDTO;
import com.aelionix.airesumebuilder.dto.StandardResponse;
import com.aelionix.airesumebuilder.service.ResumeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;


@RestController
@RequestMapping("/api/resume")
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ResumeController {

    @Autowired
    private final ResumeService resumeService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/id/{resumeId}")
    public ResponseEntity<ResumeResponseDTO> getResumeById(@PathVariable Long resumeId) {
        ResumeResponseDTO resume = resumeService.getResumeById(resumeId);
        return ResponseEntity.ok(resume);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ResumeResponseDTO>> getResumesByUserCriteria(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String title) {

        if (Stream.of(userName, email, title).allMatch(Objects::isNull)) {
            return ResponseEntity.badRequest().build();
        }

        List<ResumeResponseDTO> resumes = resumeService.findResumesByUserCriteria(userName, email, title);
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResumeResponseDTO>> getResumesByUserId(@PathVariable Long userId) {
        List<ResumeResponseDTO> resumes = resumeService.getResumesByUserId(userId);
        return ResponseEntity.ok(resumes);
    }


    @PostMapping
    public ResponseEntity<StandardResponse> uploadResume(@RequestBody ResumeRequestDTO resumeRequest) {
        ResumeResponseDTO resume = resumeService.saveResume(resumeRequest);
        StandardResponse response = new StandardResponse("success", 201, "Resume uploaded successfully", resume);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{resumeId}")
    public ResponseEntity<StandardResponse> updateResume(@PathVariable Long resumeId,
                                                         @RequestBody ResumeRequestDTO resumeRequest) {
        ResumeResponseDTO resume = resumeService.updateResume(resumeId, resumeRequest);
        StandardResponse response = new StandardResponse("success", 200, "Resume updated successfully", resume);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
