package com.ems.bpm_comparisontable.pojos;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReturnProjectAppendItemHead {

    private int headId;

    private String createdBy;

    private LocalDateTime createdDate;

    public ReturnProjectAppendItemHead(int headId, String createdBy, LocalDateTime createdDate) {
        this.headId = headId;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }
}
