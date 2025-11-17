package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.ProjectAppendItemHead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectAppentItemHeadRepository extends JpaRepository<ProjectAppendItemHead, Integer> {

    List<ProjectAppendItemHead> findAllByProjectIdAndEnabled(Integer projectId, boolean enabled);
}
