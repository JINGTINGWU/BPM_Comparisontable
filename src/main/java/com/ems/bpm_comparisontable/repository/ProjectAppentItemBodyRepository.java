package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.ProjectAppendItemBody;
import com.ems.bpm_comparisontable.model.ProjectAppendItemBodyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectAppentItemBodyRepository extends JpaRepository<ProjectAppendItemBody, ProjectAppendItemBodyId> {

    List<ProjectAppendItemBody> findAllByIdHeadId(Integer projectId);
}
