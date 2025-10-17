package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OwnerContractHeader {
    /**
     * 工程名稱
     */
    private String projectName;

    /**
     * 施工地點
     */
    private String constructionSite;

    /**
     * 項次的index
     */
    private int itemHeaderIndex;
}
