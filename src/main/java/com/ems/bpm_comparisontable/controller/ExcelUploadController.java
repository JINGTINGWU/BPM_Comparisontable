package com.ems.bpm_comparisontable.controller;

import com.ems.bpm_comparisontable.enums.OperationType;
import com.ems.bpm_comparisontable.enums.UploadExcelType;
import com.ems.bpm_comparisontable.model.OperationHistory;
import com.ems.bpm_comparisontable.pojos.ParseExcelResult;
import com.ems.bpm_comparisontable.repository.OperationHistoryRepository;
import com.ems.bpm_comparisontable.service.ExcelParseService;
import com.ems.bpm_comparisontable.service.ProjectDataService;
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
@RequestMapping("/api/upload/")
@CrossOrigin(origins = "*")
public class ExcelUploadController {

    // 上傳目錄
    @Value("${uploadFolder.excel.OwnerContract}")
    private String UPLOAD_DIR_OwnerContract;

    @Value("${uploadFolder.excel.SettlementDetails}")
    private String UPLOAD_DIR_SettlementDetails;

    @Value("${uploadFolder.excel.ContractorContract}")
    private String UPLOAD_DIR_ContractorContract;

    @Value("${uploadFolder.excel.AppendWorkItems}")
    private String UPLOAD_DIR_AppendWorkItems;



    @Autowired
    private OperationHistoryRepository operationHistoryRepository;

    @Autowired
    private ExcelParseService excelParseService;

    @Autowired
    private ProjectDataService projectDataService;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @PostMapping("/excel")
    public ResponseEntity<Map<String, Object>> uploadExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam("type") String type,
            @RequestParam("selectedProjectName") String selectedProjectName) {
        Map<String, Object> response = new HashMap<>();
        UploadExcelType uploadExcelType = UploadExcelType.Unknown;
        OperationType operationType = OperationType.Unknown;
        String originalFilename = null;
        String newFilename = null;
        try {
            // 1. 檢查檔案是否為空
            if (file.isEmpty()) {
                response.put("success", false);
                response.put("message", "檔案不能為空");
                return ResponseEntity.badRequest().body(response);
            }

            // 2. 檢查檔案類型
            originalFilename = file.getOriginalFilename();
            if (originalFilename == null ||
                    (!originalFilename.endsWith(".xlsx") && !originalFilename.endsWith(".xls"))) {
                response.put("success", false);
                response.put("message", "只能上傳 Excel 檔案 (.xlsx 或 .xls)");
                return ResponseEntity.badRequest().body(response);
            }

            // 3. 建立上傳目錄（如果不存在）
            String UPLOAD_DIR = null;
            if("ContractorContract".equals(type) ||
                    "OwnerContract".equals(type) ||
                    "SettlementDetails".equals(type) ||
                    "AppendWorkItems".equals(type))
            {
                File uploadDirFile = null;
                if("ContractorContract".equals(type)){
                    UPLOAD_DIR = UPLOAD_DIR_ContractorContract;
                } else if("OwnerContract".equals(type)){
                    UPLOAD_DIR = UPLOAD_DIR_OwnerContract;
                } else if("AppendWorkItems".equals(type)){ //追加工項
                    UPLOAD_DIR = UPLOAD_DIR_AppendWorkItems;
                } else {
                    UPLOAD_DIR = UPLOAD_DIR_SettlementDetails;
                }
                uploadDirFile = new File(UPLOAD_DIR);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }
            } else {
                response.put("success", false);
                response.put("message", "type只能是「ContractorContract」、「OwnerContract」、「SettlementDetails」");
                return ResponseEntity.badRequest().body(response);
            }

            // 4. 生成唯一檔名（避免檔案覆蓋）
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss_n"));
            newFilename = timestamp + "_" + originalFilename;

            // 5. 儲存檔案
            Path filePath = Paths.get(UPLOAD_DIR + newFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 6. 解析和儲存至資料庫
            // Type 不會是 Unknown，上面會檢查
            switch (type) {
                case "ContractorContract":
                    uploadExcelType = UploadExcelType.ContractorContract;
                    operationType = OperationType.UPLOAD_EXCEL_ContractorContract;
                    break;
                case "OwnerContract":
                    uploadExcelType = UploadExcelType.OwnerContract;
                    operationType = OperationType.UPLOAD_EXCEL_OwnerContract;
                    break;
                case "SettlementDetails":
                    uploadExcelType = UploadExcelType.SettlementDetails;
                    operationType = OperationType.UPLOAD_EXCEL_SettlementDetails;
                    break;
                case "AppendWorkItems": //追加工項
                    uploadExcelType = UploadExcelType.AppendWorkItems;
                    operationType = OperationType.UPLOAD_EXCEL_AppendWorkItems;
                    break;
            }

            // 6.1 解析EXCEL、儲存至資料庫
            ParseExcelResult result = excelParseService.parseAndSaveExcelFile(selectedProjectName, filePath.toFile(), uploadExcelType, userId);

            // 6.2 記錄操作至DB
            OperationHistory history = new OperationHistory();
            history.setUserId(userId);
            history.setOperation(operationType);
            history.setRecordTime(LocalDateTime.now());
            history.setUploadNewFileName(newFilename);
            history.setUploadOriginalFileName(originalFilename);
            history.setResult(result.getMessage());
            operationHistoryRepository.save(history);

            // 6.3 上傳失敗的要刪除檔案
            if(!result.isSuccess()) {
                Files.delete(filePath);
            }

            // 7. 回傳成功訊息
            response.put("success", result.isSuccess());
            response.put("message", result.getMessage());
            response.put("filename", newFilename);
            response.put("filepath", filePath.toString());
            response.put("size", file.getSize());
            if(result.getProjectId() != null) {
                response.put("projectAllData", projectDataService.getProjectAllData(result.getProjectId()));
            }
//            System.out.println(response);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            // 記錄操作至DB
            OperationHistory history = new OperationHistory();
            history.setUserId(userId);
            history.setOperation(operationType);
            history.setRecordTime(LocalDateTime.now());
            history.setUploadNewFileName(newFilename);
            history.setUploadOriginalFileName(originalFilename);
            history.setResult("檔案上傳失敗: " + e.getMessage().substring(0, 450));
            operationHistoryRepository.save(history);

            response.put("success", false);
            response.put("message", "檔案上傳失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
