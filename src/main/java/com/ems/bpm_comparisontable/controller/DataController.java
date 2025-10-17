package com.ems.bpm_comparisontable.controller;

import com.ems.bpm_comparisontable.enums.OperationType;
import com.ems.bpm_comparisontable.enums.UploadExcelType;
import com.ems.bpm_comparisontable.model.OperationHistory;
import com.ems.bpm_comparisontable.pojos.ParseExcelResult;
import com.ems.bpm_comparisontable.pojos.ReturnProjectAllData;
import com.ems.bpm_comparisontable.repository.OperationHistoryRepository;
import com.ems.bpm_comparisontable.service.ExcelService;
import com.ems.bpm_comparisontable.service.ProjectDataService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;

@RestController
@RequestMapping("/api/data/")
@CrossOrigin(origins = "*")
public class DataController {

    @Autowired
    private OperationHistoryRepository operationHistoryRepository;

    @Autowired
    private ProjectDataService projectDataService;

    @PostMapping("/getProjectAllData")
    public ResponseEntity<Map<String, Object>> getProjectAllData(
            @RequestParam("userId") String userId,
            @RequestParam("projectId") int projectId) {
        Map<String, Object> response = new HashMap<>();

        Optional<ReturnProjectAllData> optReturnProjectAllData = projectDataService.getProjectAllData(projectId);
        if(optReturnProjectAllData.isPresent()) {
            ReturnProjectAllData  returnProjectAllData = optReturnProjectAllData.get();

            response.put("success", true);
            response.put("message", "成功");
            response.put("projectAllData", returnProjectAllData);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "查無此專案");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
