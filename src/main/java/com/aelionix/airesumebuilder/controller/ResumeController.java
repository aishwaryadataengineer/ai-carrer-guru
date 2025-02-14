package com.aelionix.airesumebuilder.controller;

import com.aelionix.airesumebuilder.model.Resume;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping
    public Resume getResume() throws IOException {
        // Load the JSON file from classpath
        Path jsonPath = new ClassPathResource("resume.json").getFile().toPath();

        // Read JSON content and map to Resume class
        return objectMapper.readValue(Files.readString(jsonPath), Resume.class);
    }
}
