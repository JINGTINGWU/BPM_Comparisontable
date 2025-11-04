package com.ems.bpm_comparisontable.controller;

import com.ems.bpm_comparisontable.pojos.ProjectRequest;
import com.ems.bpm_comparisontable.service.ExcelCreateService;
import com.ems.bpm_comparisontable.service.ExcelParseService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@CrossOrigin(
        origins = "http://localhost:63343",  // 或指定特定域名 {"http://localhost:3000", "http://localhost:8080"}
        methods = {RequestMethod.GET, RequestMethod.POST},
        allowedHeaders = "*",
        exposedHeaders = {"Content-Disposition", "Content-Type", "Content-Length"},
        allowCredentials = "true",
        maxAge = 3600
)
@RestController
@RequestMapping("/api/download/")
public class ExcelDownloadController {

    @Autowired
    private ExcelCreateService excelCreateService;

    @PostMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel(@RequestBody ProjectRequest request,
                                                HttpServletResponse response) {
        try {
            // 生成 Excel
            Optional<ByteArrayOutputStream> optOutputStream = excelCreateService.generateExcel(request.getProjectName());
            if(optOutputStream.isPresent()) {
                ByteArrayOutputStream outputStream = optOutputStream.get();
                // 設定響應標頭
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                headers.setContentDispositionFormData("attachment",
                        "對應表.xlsx");

                return ResponseEntity.ok()
                        .headers(headers)
                        .body(outputStream.toByteArray());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
