package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.UploadHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadHistoryRepository extends JpaRepository<UploadHistory, Integer> {
}
