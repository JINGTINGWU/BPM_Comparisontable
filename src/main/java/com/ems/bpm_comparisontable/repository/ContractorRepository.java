package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractorRepository extends JpaRepository<Contractor, Integer> {

    Optional<Contractor> findByName(String name);
}
