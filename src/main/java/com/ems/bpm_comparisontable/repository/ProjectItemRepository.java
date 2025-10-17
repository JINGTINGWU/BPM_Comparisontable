package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.Project;
import com.ems.bpm_comparisontable.model.ProjectItem;
import com.ems.bpm_comparisontable.model.ProjectItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectItemRepository extends JpaRepository<ProjectItem, ProjectItemId> {

    List<ProjectItem> findByIdProjectIdOrderByIdSequenceAsc(int projectId);
}
