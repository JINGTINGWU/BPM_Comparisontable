package com.ems.bpm_comparisontable.pojos;

import com.ems.bpm_comparisontable.model.Project;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class ReturnProjectData {

    private ReturnProjectMain project;

    /** 追加工項Head */
    private List<ReturnProjectAppendItemHead> appendItemHeads;

    /** 包商Head */
    private List<ReturnProjectContractor> contractors;

    private List<LinkedList<String>> bodyDetail;



    public ReturnProjectData(){}

    public void loadProject(Project project){
        this.project = new ReturnProjectMain(project);
    }
}
