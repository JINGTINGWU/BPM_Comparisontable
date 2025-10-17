package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Optional;

@Setter
@Getter
@ToString
public class ParseExcelResult {

    private boolean isSuccess;

    private String message;

    private Integer projectId;

}
