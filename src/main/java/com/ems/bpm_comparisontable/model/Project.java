package com.ems.bpm_comparisontable.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * EF/BPM專案案號
     */
    @Column(name = "project_no")
    private String projectNo;

    /**
     * PMS專案案號
     */
    @Column(name = "pms_project_no")
    private String pmsProjectNo;

    /**
     * 工程名稱
     */
    @Column(name = "project_name")
    private String projectName;

    /**
     * 工程編號
     */
    @Column(name = "project_number")
    private String projectNumber;

    /**
     * 施工地點
     */
    @Column(name = "construction_site")
    private String constructionSite;

    /**
     * 業主合約總額
     */
    private String amount;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Column(name = "modified_by")
    private String modifiedBy;
}