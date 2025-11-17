package com.ems.bpm_comparisontable.controller;

import com.ems.bpm_comparisontable.pojos.ReturnProjectAllData;
import com.ems.bpm_comparisontable.pojos.ReturnProjectData;
import com.ems.bpm_comparisontable.repository.OperationHistoryRepository;
import com.ems.bpm_comparisontable.service.GetProjectDataService;
import com.ems.bpm_comparisontable.service.ProjectDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private GetProjectDataService getProjectDataService;

    @PostMapping("/getProjectAllData")
    public ResponseEntity<Map<String, Object>> getProjectAllData(
            @RequestParam("userId") String userId,
            @RequestParam("projectId") int projectId) {
        System.out.println("A");
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

    @PostMapping("/aaa")
    public ResponseEntity<Map<String, Object>> aaa(
            @RequestParam("userId") String userId,
            @RequestParam("projectId") int projectId) {
        System.out.println("B");
        Map<String, Object> response = new HashMap<>();

        Optional<ReturnProjectData> optReturnProjectData = getProjectDataService.getProjectAllData(projectId);

        if(optReturnProjectData.isPresent()) {
            ReturnProjectData  returnProjectData = optReturnProjectData.get();

            response.put("success", true);
            response.put("message", "成功");
            response.put("data", returnProjectData);
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "查無此專案");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
