package com.ems.bpm_comparisontable.model;

import com.ems.bpm_comparisontable.pojos.ContractorItem;
import com.ems.bpm_comparisontable.pojos.OwnerContractItem;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Project_Append_Item")
public class ProjectAppendItem {

    public ProjectAppendItem() {}
    public ProjectAppendItem(int projectId, ContractorItem contractorItem) {
        ProjectAppendItemId id = new ProjectAppendItemId();
        id.setProjectId(projectId);
        id.setItemDescription(contractorItem.getItemDescription());
        this.id = id;
        this.unitPrice = contractorItem.getUnitPrice();
    }

    @EmbeddedId
    private ProjectAppendItemId id;

    /** 單價 */
    @Column(name = "unit_price")
    private String unitPrice;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}