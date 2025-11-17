package com.ems.bpm_comparisontable.model;

import com.ems.bpm_comparisontable.pojos.ContractorItem;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Project_Append_Item_Body")
public class ProjectAppendItemBody {

    public ProjectAppendItemBody() {}
//    public ProjectAppendItemBody(int headId, String itemDescription, String unitPrice) {
//        ProjectAppendItemBodyId id = new ProjectAppendItemBodyId();
//        id.setHeadId(headId);
//        id.setItemDescription(itemDescription);
//        this.id = id;
//        this.unitPrice = unitPrice;
//    }

    public String getIdItemDescription(){
        return this.getId().getItemDescription();
    }

    @EmbeddedId
    private ProjectAppendItemBodyId id;

    /** 單價 */
    @Column(name = "unit_price")
    private String unitPrice;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}