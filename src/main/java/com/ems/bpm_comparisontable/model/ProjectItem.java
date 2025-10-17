package com.ems.bpm_comparisontable.model;

import com.ems.bpm_comparisontable.pojos.OwnerContractItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Project_Item")
public class ProjectItem {

    public ProjectItem() {}
    public ProjectItem(int projectId, OwnerContractItem ownerContractItem) {
        ProjectItemId id = new ProjectItemId();
        id.setProjectId(projectId);
        id.setSequence(ownerContractItem.getSequence());
        this.id = id;
        this.item = ownerContractItem.getItem();
        this.itemDescription = ownerContractItem.getItemDescription();
        this.unit = ownerContractItem.getUnit();
        this.quantity = ownerContractItem.getQuantity();
        this.unitPrice = ownerContractItem.getUnitPrice();
        this.complexPrice = ownerContractItem.getComplexPrice();
        this.remarks = ownerContractItem.getRemarks();
    }

    @EmbeddedId
    private ProjectItemId id;

    /** 項次 */
    private String item;

    /** 項目及說明 */
    @Column(name = "item_description")
    private String itemDescription;

    /** 單位 */
    private String unit;

    /** 數量 */
    private String quantity;

    /** 單價 */
    @Column(name = "unit_price")
    private String unitPrice;

    /** 複價 */
    @Column(name = "complex_price")
    private String complexPrice;

    /** 備註 */
    private String remarks;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}