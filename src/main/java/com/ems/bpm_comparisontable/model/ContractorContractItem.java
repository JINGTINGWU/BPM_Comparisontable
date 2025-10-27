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
@Table(name = "Contractor_Contract_Item")
public class ContractorContractItem {

    public ContractorContractItem() {}
    public ContractorContractItem(int projectId, int contractorId, int sequence, ContractorItem contractorItem) {
        ContractorContractItemId id = new ContractorContractItemId();
        id.setProjectId(projectId);
        id.setContractorId(contractorId);
        id.setItemDescription(contractorItem.getItemDescription());
        this.id = id;
        this.item = contractorItem.getItem();
        this.unit = contractorItem.getUnit();
        this.quantity = contractorItem.getQuantity();
        this.unitPrice = contractorItem.getUnitPrice();
        this.complexPrice = contractorItem.getComplexPrice();
        this.sequence = sequence;
    }

    @EmbeddedId
    private ContractorContractItemId id;

    /** 項次 */
    private String item;

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

    private int sequence;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}