package com.ems.bpm_comparisontable.pojos;

import com.ems.bpm_comparisontable.model.Project;
import com.ems.bpm_comparisontable.model.ProjectItem;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ReturnProjectAllData {

    private ReturnProjectMain project;

    private List<ReturnProjectItem> projectItems;
}
