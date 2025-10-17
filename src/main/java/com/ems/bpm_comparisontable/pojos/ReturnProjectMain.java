package com.ems.bpm_comparisontable.pojos;

import com.ems.bpm_comparisontable.model.Project;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReturnProjectMain {
    private String projectNo;

    /** PMS專案案號 */
    private String pmsProjectNo;

    /** 工程名稱 */
    private String projectName;

    /** 工程編號 */
    private String projectNumber;

    /** 施工地點 */
    private String constructionSite;

    /** 業主合約總額 */
    private String amount;

    public ReturnProjectMain(Project project){
        projectNo = project.getProjectNo();
        pmsProjectNo = project.getPmsProjectNo();
        projectName = project.getProjectName();
        projectNumber = project.getProjectNumber();
        constructionSite = project.getConstructionSite();
        amount = project.getAmount();
    }
}