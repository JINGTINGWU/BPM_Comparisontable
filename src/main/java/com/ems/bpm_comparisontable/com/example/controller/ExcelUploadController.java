package com.ems.bpm_comparisontable.com.example.controller;

import com.ems.bpm_comparisontable.model.UploadHistory;
import com.ems.bpm_comparisontable.repository.UploadHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/uploadExcel")
@CrossOrigin(origins = "*")
public class ExcelUploadController {

    // 上傳目錄
    @Value("${uploadFolder.excel.OwnerContract}")
    private String UPLOAD_DIR_OwnerContract;

    @Autowired
    private UploadHistoryRepository uploadHistoryRepository;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PostMapping("/OwnerContract")
    public ResponseEntity<Map<String, Object>> uploadExcel(
            @RequestParam("file") MultipartFile file) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 1. 檢查檔案是否為空
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "檔案不能為空");
                return ResponseEntity.badRequest().body(response);
            }

            // 2. 檢查檔案類型
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null ||
                    (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
                response.put("success", false);
                response.put("message", "只能上傳 Excel 檔案 (.xlsx 或 .xls)");
                return ResponseEntity.badRequest().body(response);
            }

            // 3. 建立上傳目錄（如果不存在）
            File uploadDir = new File(UPLOAD_DIR_OwnerContract);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 4. 生成唯一檔名（避免檔案覆蓋）
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss_n"));
            String newFilename = timestamp + "_" + originalFilename;

            // 5. 儲存檔案
            Path filePath = Paths.get(UPLOAD_DIR_OwnerContract + newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 6. 回傳成功訊息
            response.put("success", true);
            response.put("message", "檔案上傳成功");
            response.put("filename", newFilename);
            response.put("filepath", filePath.toString());
            response.put("size", file.getSize());


            UploadHistory uh = new UploadHistory();
            uh.setUploadDate(LocalDateTime.now());
            uh.setUser("07024");
            uh.setFileName(newFilename);
            uh.setFileTyoe("OwnerContract");
            uploadHistoryRepository.save(uh);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "檔案上傳失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
