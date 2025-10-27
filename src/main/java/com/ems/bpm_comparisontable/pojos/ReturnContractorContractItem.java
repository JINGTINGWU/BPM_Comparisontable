package com.ems.bpm_comparisontable.pojos;

import com.ems.bpm_comparisontable.model.ContractorContractItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReturnContractorContractItem {

    private int contractorId;

    /** 項次 */
    private String item;

    /** 項目及說明 */
    private String itemDescription;

    /** 單位 */
    private String unit;

    /** 數量 */
    private String quantity;

    /** 單價 */
    private String unitPrice;

    /** 複價 */
    private String complexPrice;

    public ReturnContractorContractItem(ContractorContractItem item) {
        this.contractorId = item.getId().getContractorId();
        this.item = item.getItem();
        this.itemDescription = item.getId().getItemDescription();
        this.unit = item.getUnit();
        this.quantity = item.getQuantity();
        this.unitPrice = item.getUnitPrice();
        this.complexPrice = item.getComplexPrice();

    }
}
