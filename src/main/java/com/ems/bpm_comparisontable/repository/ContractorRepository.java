package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ContractorRepository extends JpaRepository<Contractor, Integer> {

    Optional<Contractor> findByName(String name);

    @Query(value = """
            select * from Contractor c
            where c.id  in (
            	select distinct contractor_id 
            	from  Contractor_Contract_Item
            	where project_id = ?1
            )
            order by id
            """,
            nativeQuery = true)
    List<Contractor> getAllContractorsOfProject(int projectId);
}
