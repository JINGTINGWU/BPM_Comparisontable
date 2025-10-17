package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.OperationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationHistoryRepository extends JpaRepository<OperationHistory, Integer> {
}
