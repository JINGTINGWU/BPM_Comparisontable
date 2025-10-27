package com.ems.bpm_comparisontable.controller;

import com.ems.bpm_comparisontable.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/download/")
public class ExcelDownloadController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel(@RequestParam("projectName") String projectName) {
        try {
            // 生成 Excel
            byte[] excelBytes = generateExcel(request);

            // 設定檔名
            String filename = "report_" + LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            // 設定 HTTP Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelBytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
