package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.ProjectAppendItem;
import com.ems.bpm_comparisontable.model.ProjectAppendItemId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectAppentItemRepository extends JpaRepository<ProjectAppendItem, ProjectAppendItemId> {

}
