package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Setter
@Getter
@ToString
public class ReturnProjectAllData {

    /** 專案 */
    private ReturnProjectMain project;
    /** 專案項目 */
    private List<ReturnProjectItem> projectItems;
    /** 包商 */
    private List<ReturnContractor>  contractors;
    /** 包商合約項目 */
    private Map<Integer, List<ReturnContractorContractItem>> contractorContractItemMap;

}
