package com.ems.bpm_comparisontable.model;

import com.ems.bpm_comparisontable.enums.OperationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Operation_History")
public class OperationHistory {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    /** 人員工號 */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /** 做什麼事情 */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType operation;

    /** 作業時間 */
    @Column(name = "record_time", nullable = false)
    private LocalDateTime recordTime;

    /** 上傳檔案的新名稱（如果是上傳作業才需要） */
    @Column(name = "upload_new_file_name")
    private String uploadNewFileName;

    /** 上傳檔案的原始名稱（如果是上傳作業才需要） */
    @Column(name = "upload_original_file_name")
    private String uploadOriginalFileName;

    /** 返回結果 */
    @Column(nullable = false)
    private String result;
}
