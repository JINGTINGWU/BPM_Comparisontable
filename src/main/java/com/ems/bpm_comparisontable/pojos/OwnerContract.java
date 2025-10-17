package com.ems.bpm_comparisontable.pojos;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;

@Setter
@Getter
@ToString
public class OwnerContract {

    private OwnerContractHeader header;

    private LinkedList<OwnerContractItem> items;

}
