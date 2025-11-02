package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.enums.UploadExcelType;
import com.ems.bpm_comparisontable.model.Contractor;
import com.ems.bpm_comparisontable.model.Project;
import com.ems.bpm_comparisontable.pojos.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExcelParseService {

    @Autowired
    private ProjectDataService projectDataService;

    FormulaEvaluator evaluator = null;

    public ParseExcelResult parseAndSaveExcelFile(File file, UploadExcelType type, String userId) throws IOException, IllegalArgumentException {
        ParseExcelResult result = new ParseExcelResult();

        try (InputStream is = Files.newInputStream(file.toPath())) {
            Workbook workbook;
            String fileName = file.getName();
            System.out.println(fileName);
            // 根據檔案類型建立 Workbook
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else if (fileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(is);
            } else {
                throw new IllegalArgumentException("不支援的檔案格式");
            }
            evaluator = workbook.getCreationHelper().createFormulaEvaluator();

            if(type == UploadExcelType.OwnerContract) {
                result = parseOwnerContract(workbook, userId);
            } else if(type == UploadExcelType.ContractorContract) {
                String contractorName = fileName.replace(".xlsx", "").replace(".xls", "");
                if(!contractorName.endsWith("_簽約單價")) {
                    result.setSuccess(false);
                    result.setMessage("檔名必須是「_簽約單價」結尾");
                } else {
                    System.out.println(contractorName);
                    contractorName = contractorName.split("_")[2];
                    Contractor contractor = projectDataService.saveContractor(contractorName, userId);
                    result = parseContractorContract(workbook, userId, contractor);
                }
            }

            workbook.close();
            if(type == UploadExcelType.OwnerContract ||
                    type == UploadExcelType.ContractorContract) {
                return result;
            }
        }

        result.setSuccess(false);
        if(type == UploadExcelType.ContractorContract) {
            result.setMessage("上傳的Excel檔案不是「包商合約」");
        } else if(type == UploadExcelType.SettlementDetails) {
            result.setMessage("上傳的Excel檔案不是「結算明細」");
        }
        return result;
    }

    private ParseExcelResult parseOwnerContract(Workbook workbook, String userId){
        ParseExcelResult result = new ParseExcelResult();
        Sheet sheet = null;
        String ownerContractAmount = null;
        for(int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheet = workbook.getSheetAt(i);
            if("契約總表".equals(sheet.getSheetName())) { //要取得總價
                ownerContractAmount = parseOwnerContractAmount(sheet);
            } else if("契約詳細表".equals(sheet.getSheetName())) {
                OwnerContract ownerContract = parseOwnerContract(sheet);
                if(!ownerContract.getHeader().getConstructionSite().isEmpty() &&
                        !ownerContract.getItems().isEmpty()) {
                    //檢查專案是否重複
                    Optional<Project> existedProject = projectDataService.findProject(ownerContract.getHeader().getProjectName());
                    if(existedProject.isPresent()) {
                        result.setSuccess(false);
                        result.setMessage("已存在相同「工程名稱」");
                        result.setProjectId(existedProject.get().getId());
                        System.out.println(result);
                        return result;
                    } else {
                        result.setSuccess(true);
                        LocalDateTime now = LocalDateTime.now();
                        // 儲存 project
                        Project savedProject = projectDataService.createNewProject(ownerContract.getHeader(), ownerContractAmount, userId, now);
                        // 儲存 item
                        for(int j=0; j<ownerContract.getItems().size(); j++) {
                            projectDataService.createNewProjectItem(savedProject.getId(), ownerContract.getItems().get(j), userId, now);
                        }

                        result.setMessage("檔案上傳成功");
                        result.setProjectId(savedProject.getId());

                        return result;
                    }
                } else {
                    result.setSuccess(false);
                    result.setMessage("解析「業主合約」無法取得資料");
                    return result;
                }
            }
        }

        result.setSuccess(false);
        result.setMessage("解析失敗，無「契約詳細表」頁籤");
        return result;
    }

    private ParseExcelResult parseContractorContract(Workbook workbook, String userId, Contractor contractor){
        ParseExcelResult result = new ParseExcelResult();
        Sheet sheet = workbook.getSheetAt(0);
        ContractorContract cc = parseContractorContract(sheet);

        if(!cc.getItems().isEmpty() && cc.getProjectName() != null) {
            System.out.println(cc.getProjectName());
            Optional<Project> optProject = projectDataService.findProject(cc.getProjectName());
            if(optProject.isEmpty()) {
                result.setSuccess(false);
                result.setMessage("查無此專案：" + cc.getProjectName());
                return result;
            }

            result.setSuccess(true);
            LocalDateTime now = LocalDateTime.now();
            Project project = optProject.get();


            projectDataService.removeContractorContractItems(project.getId(), contractor.getId());

            // 儲存 item
            System.out.println("cc.getItems().size():"+cc.getItems().size());
            int index = 0;
            for(int i=0; i<cc.getItems().size(); i++) {
                if("合計".equals(cc.getItems().get(i).getItemDescription()) ||
                        "稅額".equals(cc.getItems().get(i).getItemDescription()) ||
                        "總計".equals(cc.getItems().get(i).getItemDescription()) ) {
                  continue;
                }
                projectDataService.createNewContractorContractItem(project.getId(), contractor.getId(), ++index, cc.getItems().get(i), userId, now);
            }

            result.setMessage("檔案上傳成功");
            result.setProjectId(project.getId());

            return result;
        } else {
            result.setSuccess(false);
            result.setMessage("解析「廠商簽約」無法取得資料");
            return result;
        }
    }

    private String parseOwnerContractAmount(Sheet sheet) {
        int lastRowNum = sheet.getLastRowNum();
        for(int i = 7; i < lastRowNum; i++) {
            if("總價".equals(getCellValue(sheet.getRow(i).getCell(1)))){
                return getCellValue(sheet.getRow(i).getCell(5));
            }
        }
        return "";
    }

    private OwnerContract parseOwnerContract(Sheet sheet) {
        LinkedList<OwnerContractItem> items = new LinkedList<>();
        OwnerContractHeader ownerContractHeader = new OwnerContractHeader();
        for(int i = 0; i < 10; i++) {
            if("工程名稱".equals(getCellValue(sheet.getRow(i).getCell(0)))){
                ownerContractHeader.setProjectName(sheet.getRow(i).getCell(1).getStringCellValue());
            }
            if("施工地點".equals(getCellValue(sheet.getRow(i).getCell(0)))){
                ownerContractHeader.setConstructionSite(sheet.getRow(i).getCell(1).getStringCellValue());
            }
            if("項次".equals(getCellValue(sheet.getRow(i).getCell(0)).replaceAll(" ", ""))){
                ownerContractHeader.setItemHeaderIndex(i);
            }
        }

        int lastRowNum = sheet.getLastRowNum();
        int sequence = 0;
        for(int i = ownerContractHeader.getItemHeaderIndex() + 1; i < lastRowNum; i++) {
            OwnerContractItem item = new OwnerContractItem();
            item.setItem(getCellValue(sheet.getRow(i).getCell(0)).replaceAll(" ", ""));
            item.setItemDescription(getCellValue(sheet.getRow(i).getCell(1)));
            item.setUnit(getCellValue(sheet.getRow(i).getCell(2)));
            item.setQuantity(getCellValue(sheet.getRow(i).getCell(3)));
            item.setUnitPrice(getCellValue(sheet.getRow(i).getCell(4)));
            item.setComplexPrice(getCellValue(sheet.getRow(i).getCell(5)));
            item.setRemarks(getCellValue(sheet.getRow(i).getCell(6)));
            if(item.getItemDescription().isEmpty()) {
            } else if("工程名稱".equals(item.getItem()) || "施工地點".equals(item.getItem()) || "項次".equals(item.getItem())){
            } else {
                item.setSequence(++sequence);
                items.add(item);
            }
        }

        OwnerContract ownerContract = new OwnerContract();
        ownerContract.setHeader(ownerContractHeader);
        ownerContract.setItems(items);
        return ownerContract;
    }

    private ContractorContract parseContractorContract(Sheet sheet) {
        LinkedList<ContractorItem> items = new LinkedList<>();

        int lastRowNum = sheet.getLastRowNum();
        String projectName = null;
        for(int i = 0; i < lastRowNum; i++) {
            ContractorItem item = new ContractorItem();
            item.setItem(getCellValue(sheet.getRow(i).getCell(0)).replaceAll(" ", ""));
            item.setItemDescription(getCellValue(sheet.getRow(i).getCell(1)));

            if(item.getItemDescription().isEmpty()) {
                continue;
            } else if("施工地點".equals(item.getItem()) || "項次".equals(item.getItem())){
                continue;
            } else if("工程名稱".equals(item.getItem())){
                projectName = getCellValue(sheet.getRow(i).getCell(1));
            } else {
                item.setUnit(getCellValue(sheet.getRow(i).getCell(2)));
                item.setQuantity(getCellValue(sheet.getRow(i).getCell(3)));
                item.setUnitPrice(getCellValue(sheet.getRow(i).getCell(4)));
                item.setComplexPrice(getCellValue(sheet.getRow(i).getCell(5)));
                items.add(item);
            }
        }

        ContractorContract cc = new ContractorContract();
        cc.setItems(items);
        cc.setProjectName(projectName);
        return cc;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 避免科學記號表示法
                    double numericValue = cell.getNumericCellValue();
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    } else {
                        return String.valueOf(numericValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                CellValue cellValue = evaluator.evaluate(cell);
                return switch (cellValue.getCellType()) {
                    case STRING -> cellValue.getStringValue();
                    case NUMERIC -> String.valueOf(cellValue.getNumberValue());
                    case BOOLEAN -> String.valueOf(cellValue.getBooleanValue());
                    default -> "";
                };
            default:
                return "";
        }
    }

}
