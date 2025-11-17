package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
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

    /** 追加工項 */
    private List<ReturnProjectAppendItemHead> appendItemHeads;

    /** 包商 */
    private List<ReturnProjectContractor>  contractors;
    /** 包商合約項目 */
    private Map<Integer, List<ReturnProjectContractorContractItem>> contractorContractItemMap;


    private List<LinkedList<String>> bodyDetail;
}
