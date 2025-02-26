package com.aelionix.airesumebuilder.service;

import com.aelionix.airesumebuilder.dto.*;
import com.aelionix.airesumebuilder.model.CandidateResume;
import com.aelionix.airesumebuilder.model.Certification;
import com.aelionix.airesumebuilder.model.Education;
import com.aelionix.airesumebuilder.model.Experience;
import com.aelionix.airesumebuilder.model.Skill;
import com.aelionix.airesumebuilder.model.SkillCategory;
import com.aelionix.airesumebuilder.model.User;
import com.aelionix.airesumebuilder.repository.CandidateResumeRepository;
import com.aelionix.airesumebuilder.repository.SkillCategoryRepository;
import com.aelionix.airesumebuilder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ResumeServiceImpl implements ResumeService {

    private final UserRepository userRepository;
    private final CandidateResumeRepository candidateResumeRepository;
    private final SkillCategoryRepository skillCategoryRepository;

    @Override
    public ResumeResponseDTO saveResume(ResumeRequestDTO resumeRequest) {
        User user = userRepository.findByEmailId(resumeRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        List<CandidateResume> existingResumes = candidateResumeRepository.findByUserAndTitle(user, resumeRequest.title());
        if (!existingResumes.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A resume with title '" + resumeRequest.title() + "' already exists for this user"
            );
        }
        CandidateResume candidateResume = new CandidateResume();
        candidateResume.setUser(user);
        candidateResume.setTitle(resumeRequest.title());
        candidateResume.setSummary(resumeRequest.summary());

        if (resumeRequest.experiences() != null && !resumeRequest.experiences().isEmpty()) {
            List<Experience> experiences = resumeRequest.experiences().stream()
                    .map(dto -> mapToExperience(dto, candidateResume, user))
                    .collect(Collectors.toList());
            candidateResume.setExperiences(experiences);
        }

        if (resumeRequest.education() != null && !resumeRequest.education().isEmpty()) {
            List<Education> educations = resumeRequest.education().stream()
                    .map(dto -> mapToEducation(dto, candidateResume))
                    .collect(Collectors.toList());
            candidateResume.setEducations(educations);
        }

        if (resumeRequest.certifications() != null && !resumeRequest.certifications().isEmpty()) {
            List<Certification> certifications = resumeRequest.certifications().stream()
                    .map(dto -> mapToCertification(dto, candidateResume, user))
                    .collect(Collectors.toList());
            candidateResume.setCertifications(certifications);
        }

        if (resumeRequest.skills() != null && !resumeRequest.skills().isEmpty()) {
            Set<Skill> skills = new HashSet<>();
            for (Map.Entry<String, List<String>> entry : resumeRequest.skills().entrySet()) {
                String categoryName = entry.getKey();
                for (String skillName : entry.getValue()) {
                    skills.add(mapToSkill(categoryName, skillName));
                }
            }
            candidateResume.setSkills(skills);
        }

        CandidateResume savedResume = candidateResumeRepository.save(candidateResume);
        return convertToResumeResponseDTO(savedResume);
    }

    @Override
    public ResumeResponseDTO getResumeById(Long resumeId) {
        CandidateResume resume = candidateResumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Resume not found with ID: " + resumeId));
        return convertToResumeResponseDTO(resume);
    }

    @Override
    public List<ResumeResponseDTO> findResumesByUserCriteria(String userName, String email, String title) {
        List<CandidateResume> resumes;

        if (userName != null) {
            User user = userRepository.findByUserName(userName)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User not found with username: " + userName));
            resumes = candidateResumeRepository.findByUser(user);
        } else if (email != null) {
            User user = userRepository.findByEmailId(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "User not found with email: " + email));
            resumes = candidateResumeRepository.findByUser(user);
        } else {
            resumes = candidateResumeRepository.findByTitleContainingIgnoreCase(title);
        }

        return resumes.stream()
                .map(this::convertToResumeResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResumeResponseDTO> getResumesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User not found with ID: " + userId));

        List<CandidateResume> resumes = candidateResumeRepository.findByUser(user);
        return resumes.stream()
                .map(this::convertToResumeResponseDTO)
                .collect(Collectors.toList());
    }
    

    @Override
    public ResumeResponseDTO updateResume(Long resumeId, ResumeRequestDTO resumeRequest) {
        CandidateResume candidateResume = candidateResumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resume not found"));

        candidateResume.setTitle(resumeRequest.title());
        candidateResume.setSummary(resumeRequest.summary());

        if (resumeRequest.experiences() != null) {
            updateExperiences(candidateResume, resumeRequest.experiences());
        }

        if (resumeRequest.education() != null) {
            List<Education> educations = resumeRequest.education().stream()
                    .map(dto -> mapToEducation(dto, candidateResume))
                    .collect(Collectors.toList());
            candidateResume.setEducations(educations);
        }

        if (resumeRequest.certifications() != null) {
            List<Certification> certifications = resumeRequest.certifications().stream()
                    .map(dto -> mapToCertification(dto, candidateResume, candidateResume.getUser()))
                    .collect(Collectors.toList());
            candidateResume.setCertifications(certifications);
        }

        if (resumeRequest.skills() != null) {
            Set<Skill> skills = new HashSet<>();
            for (Map.Entry<String, List<String>> entry : resumeRequest.skills().entrySet()) {
                String categoryName = entry.getKey();
                for (String skillName : entry.getValue()) {
                    skills.add(mapToSkill(categoryName, skillName));
                }
            }
            candidateResume.setSkills(skills);
        }

        CandidateResume updatedResume = candidateResumeRepository.save(candidateResume);
        return convertToResumeResponseDTO(updatedResume);
    }

    private void updateExperiences(CandidateResume candidateResume, List<ExperienceDTO> experienceDTOs) {
        List<Experience> currentExperiences = candidateResume.getExperiences();
        if (currentExperiences == null) {
            currentExperiences = new ArrayList<>();
        }

        Map<String, Experience> existingExperienceMap = currentExperiences.stream()
                .collect(Collectors.toMap(Experience::getCompany, exp -> exp, (exp1, exp2) -> exp1));

        List<Experience> updatedExperiences = experienceDTOs.stream()
                .map(dto -> {
                    Experience experience = existingExperienceMap.getOrDefault(dto.company(), new Experience());
                    updateExperienceFromDTO(experience, dto, candidateResume, candidateResume.getUser());
                    return experience;
                })
                .collect(Collectors.toList());

        candidateResume.setExperiences(updatedExperiences);
    }

    private void updateExperienceFromDTO(Experience experience, ExperienceDTO dto, CandidateResume candidateResume, User user) {
        experience.setCompany(dto.company());
        experience.setTitle(dto.title());
        experience.setLocation(dto.location());
        experience.setStartDate(dto.startDate());
        experience.setEndDate(dto.endDate());
        experience.setDescription(dto.description());
        experience.setCandidateResume(candidateResume);
        experience.setUser(user);
    }

    private ResumeResponseDTO convertToResumeResponseDTO(CandidateResume resume) {
        return new ResumeResponseDTO(
                resume.getResumeId(),
                convertToUserBasicDTO(resume.getUser()),
                resume.getTitle(),
                resume.getSummary(),
                convertToExperienceDTOList(resume.getExperiences()),
                convertToEducationDTOList(resume.getEducations()),
                convertToCertificationDTOList(resume.getCertifications()),
                convertToSkillsMap(resume.getSkills())
        );
    }

    private UserBasicDTO convertToUserBasicDTO(User user) {
        return new UserBasicDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmailId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getTitle(),
                user.getLocation()
        );
    }

    private Experience mapToExperience(ExperienceDTO dto, CandidateResume candidateResume, User user) {
        Experience experience = new Experience();
        experience.setCompany(dto.company());
        experience.setTitle(dto.title());
        experience.setLocation(dto.location());
        experience.setStartDate(dto.startDate());
        experience.setEndDate(dto.endDate());
        experience.setDescription(dto.description());
        experience.setCandidateResume(candidateResume);
        experience.setUser(user);
        return experience;
    }

    private Education mapToEducation(EducationDTO dto, CandidateResume candidateResume) {
        Education education = new Education();
        education.setSchoolName(dto.schoolName());
        education.setDegree(dto.degree());
        education.setFieldOfStudy(dto.fieldOfStudy());
        education.setGraduationDate(dto.graduationDate());
        education.setCandidateResume(candidateResume);
        return education;
    }

    private Certification mapToCertification(CertificationDTO dto, CandidateResume candidateResume, User user) {
        Certification cert = new Certification();
        cert.setCertificationName(dto.certificationName());
        cert.setIssuingOrganization(dto.issuingOrganization());
        cert.setIssueDate(dto.issueDate() != null ? dto.issueDate() : LocalDate.now());
        cert.setExpiryDate(dto.expiryDate());
        cert.setCertificationUrl(dto.certificationUrl());
        cert.setCandidateResume(candidateResume);
        cert.setUser(user);
        return cert;
    }

    private Skill mapToSkill(String categoryName, String skillName) {
        SkillCategory category = skillCategoryRepository.findByCategoryName(categoryName)
                .orElseGet(() -> {
                    SkillCategory newCategory = new SkillCategory();
                    newCategory.setCategoryName(categoryName);
                    return skillCategoryRepository.save(newCategory);
                });
        Skill skill = new Skill();
        skill.setSkillName(skillName);
        skill.setSkillsCategory(category);
        return skill;
    }

    private List<ExperienceDTO> convertToExperienceDTOList(List<Experience> experiences) {
        if (experiences == null) return new ArrayList<>();
        return experiences.stream()
                .map(exp -> new ExperienceDTO(
                        exp.getExperienceId(),
                        exp.getCompany(),
                        exp.getTitle(),
                        exp.getLocation(),
                        exp.getStartDate(),
                        exp.getEndDate(),
                        exp.getDescription()
                ))
                .collect(Collectors.toList());
    }

    private List<EducationDTO> convertToEducationDTOList(List<Education> educations) {
        if (educations == null) return new ArrayList<>();
        return educations.stream()
                .map(edu -> new EducationDTO(
                        edu.getEduId(),
                        edu.getSchoolName(),
                        edu.getDegree(),
                        edu.getFieldOfStudy(),
                        edu.getGraduationDate()
                ))
                .collect(Collectors.toList());
    }

    private List<CertificationDTO> convertToCertificationDTOList(List<Certification> certifications) {
        if (certifications == null) return new ArrayList<>();
        return certifications.stream()
                .map(cert -> new CertificationDTO(
                        cert.getCertificationName(),
                        cert.getIssuingOrganization(),
                        cert.getIssueDate(),
                        cert.getExpiryDate(),
                        cert.getCertificationUrl()
                ))
                .collect(Collectors.toList());
    }

    private Map<String, List<String>> convertToSkillsMap(Set<Skill> skills) {
        if (skills == null) return new HashMap<>();
        return skills.stream()
                .collect(Collectors.groupingBy(
                        skill -> skill.getSkillsCategory().getCategoryName(),
                        Collectors.mapping(Skill::getSkillName, Collectors.toList())
                ));
    }
}
