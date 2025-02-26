package com.aelionix.airesumebuilder.repository;

import com.aelionix.airesumebuilder.model.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, Long> {
    Optional<SkillCategory> findByCategoryName(String categoryName);
}
