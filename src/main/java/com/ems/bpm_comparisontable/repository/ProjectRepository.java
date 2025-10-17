package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Optional<Project> findByProjectName(String projectName);
}
