package com.ems.bpm_comparisontable.model;

import com.ems.bpm_comparisontable.pojos.ContractorItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Project_Append_Item_Head")
public class ProjectAppendItemHead {

    public ProjectAppendItemHead() {}
    public ProjectAppendItemHead(int projectId, String createdBy) {
        this.projectId = projectId;
        this.enabled = true;
        this.createdDate = LocalDateTime.now();
        this.createdBy = createdBy;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "project_id")
    private int projectId;

    private boolean enabled;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}