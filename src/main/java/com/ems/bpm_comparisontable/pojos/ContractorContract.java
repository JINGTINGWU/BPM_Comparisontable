package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class ContractorContract {

    private String projectName;

    private List<ContractorItem> items;
}
