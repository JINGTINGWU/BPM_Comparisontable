package com.ems.bpm_comparisontable.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Upload_History")
public class UploadHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @Column(name = "upload_user")
    private String user;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileTyoe;
}
