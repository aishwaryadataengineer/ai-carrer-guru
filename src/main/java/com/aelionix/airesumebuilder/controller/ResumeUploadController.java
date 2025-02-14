//package com.aelionix.airesumebuilder.controller;
//
//import com.aelionix.airesumebuilder.model.User;
//import com.aelionix.airesumebuilder.service.ResumeParserService;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/resume")
//@RequiredArgsConstructor
//public class ResumeUploadController {
//
//    private final ResumeParserService resumeParserService;
//
//    @PostMapping("/upload")
//    public ResponseEntity<Map<String, Object>> uploadResume(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
//        try {
//            Map<String, Object> parsedData = resumeParserService.parseResume(file);
//            return ResponseEntity.ok(parsedData);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
//        }
//    }
//}
