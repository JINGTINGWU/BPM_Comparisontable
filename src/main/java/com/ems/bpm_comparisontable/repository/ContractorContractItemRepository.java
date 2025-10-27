package com.ems.bpm_comparisontable.repository;

import com.ems.bpm_comparisontable.model.Contractor;
import com.ems.bpm_comparisontable.model.ContractorContractItem;
import com.ems.bpm_comparisontable.model.ContractorContractItemId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ContractorContractItemRepository extends JpaRepository<ContractorContractItem, ContractorContractItemId> {

    @Query(value = """
            select * from Contractor_Contract_Item
            where project_id = ?1 and contractor_id = ?2
            order by sequence
            """,
            nativeQuery = true)
    List<ContractorContractItem> getContractorContractItem(int projectId, int contractorId);

    @Modifying
    @Transactional
    @Query(value = """
           DELETE FROM Contractor_Contract_Item
           WHERE project_id = ?1 AND contractor_id = ?2
            """,
            nativeQuery = true)
    void removeAll(int projectId, int contractorId);
}
