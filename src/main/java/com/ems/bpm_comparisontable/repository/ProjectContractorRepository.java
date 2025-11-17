package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.ProjectContractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectContractorRepository extends JpaRepository<ProjectContractor, Integer> {

    Optional<ProjectContractor> findByName(String name);

    @Query(value = """
            select * from Project_Contractor c
            where c.id  in (
            	select distinct contractor_id 
            	from  Contractor_Contract_Item
            	where project_id = ?1
                        	AND enabled = 1
            )
            order by id
            """,
            nativeQuery = true)
    List<ProjectContractor> getAllContractorsOfProject(int projectId);
}
