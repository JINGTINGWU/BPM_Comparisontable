package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReturnProjectContractor {

    private Integer id;

    private String name;

    public ReturnProjectContractor(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
