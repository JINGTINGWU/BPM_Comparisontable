package com.ems.bpm_comparisontable.pojos;

import com.ems.bpm_comparisontable.model.Project;
import com.ems.bpm_comparisontable.model.ProjectItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReturnProjectItem {

    private int sequence;

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

    /** 備註 */
    private String remarks;

    public ReturnProjectItem(ProjectItem  projectItem) {
        sequence = projectItem.getId().getSequence();
        item = projectItem.getItem();
        itemDescription = projectItem.getItemDescription();
        unit = projectItem.getUnit();
        quantity = projectItem.getQuantity();
        unitPrice = projectItem.getUnitPrice();
        complexPrice = projectItem.getComplexPrice();
        remarks = projectItem.getRemarks();
    }
}
