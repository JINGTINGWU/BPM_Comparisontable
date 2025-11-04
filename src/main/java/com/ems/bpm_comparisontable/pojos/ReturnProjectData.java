package com.ems.bpm_comparisontable.pojos;

import com.ems.bpm_comparisontable.model.Project;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReturnProjectData {
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

    private String[] items;

    public ReturnProjectData(){}

    public void loadProject(Project project){
        this.projectNo = project.getProjectNo();
        this.pmsProjectNo = project.getPmsProjectNo();
        this.projectName = project.getProjectName();
        this.projectNumber = project.getProjectNumber();
        this.constructionSite = project.getConstructionSite();
        this.amount = project.getAmount();
    }
}
