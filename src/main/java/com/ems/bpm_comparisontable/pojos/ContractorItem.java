package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ContractorItem {
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

}
