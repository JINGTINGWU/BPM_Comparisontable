package com.ems.bpm_comparisontable.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ContractorContractItemId implements Serializable {

    @Column(name = "project_id")
    private int projectId;

    @Column(name = "contractor_id")
    private int contractorId;

    /** 項目及說明 */
    @Column(name = "item_description")
    private String itemDescription;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractorContractItemId that = (ContractorContractItemId) o;
        return projectId == that.projectId && contractorId == that.contractorId && itemDescription.equals(that.itemDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectId, contractorId, itemDescription);
    }
}
