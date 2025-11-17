package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.pojos.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExcelCreateService {
    @Autowired
    private ProjectDataService projectDataService;

    private static CellStyle STYLE_HEADER = null, STYLE_CENTER = null, STYLE_RIGHT = null;

    /**
     * 設定 CellStyle
     * @param workbook
     */
    private void buildStyle(Workbook workbook){
        Font fontBold = workbook.createFont();
        fontBold.setBold(true);

        STYLE_HEADER = workbook.createCellStyle();
        STYLE_HEADER.setFont(fontBold);
        STYLE_HEADER.setAlignment(HorizontalAlignment.CENTER);

        STYLE_CENTER = workbook.createCellStyle();
        STYLE_CENTER.setAlignment(HorizontalAlignment.CENTER);

        STYLE_RIGHT = workbook.createCellStyle();
        STYLE_RIGHT.setAlignment(HorizontalAlignment.RIGHT);
    }

    public Optional<ByteArrayOutputStream> generateExcel(String projectName) throws Exception {
        // 建立工作簿
        Workbook workbook = new XSSFWorkbook();

        buildStyle(workbook);

        Sheet sheet = workbook.createSheet("對應表");

        Optional<ReturnProjectAllData> optReturnProjectAllData = projectDataService.getProjectAllData(projectName);
        if(optReturnProjectAllData.isPresent()) {
            ReturnProjectAllData  returnProjectAllData = optReturnProjectAllData.get();
            //開始Excel建立內容
            buildOwnerMain(workbook, sheet, returnProjectAllData.getProject());
            Set<String> appendItemDescriptions = parseAppendItems(returnProjectAllData);
            buildOwnerItems(workbook, sheet, returnProjectAllData.getProjectItems());
            buildAppendItemDescriptions(sheet, returnProjectAllData.getProjectItems().size(), appendItemDescriptions);


            // 調整欄寬
            int[] widths = {10, 24, 6, 6, 10, 10, 20};
            for (int i = 0; i < widths.length; i++) {
                sheet.setColumnWidth(i, 256 * widths[i]);
            }


            // 寫入到 ByteArrayOutputStream
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();

            return Optional.of(outputStream);
        }

        return Optional.empty();
    }

    /**
     * 專案名稱、地點、金額、案號等資訊
     * @param workbook
     * @param sheet
     * @param project
     */
    private void buildOwnerMain(Workbook workbook, Sheet sheet, ReturnProjectMain project){
        Row row = sheet.createRow(0);
        row.setHeightInPoints(20);

        Cell cell = row.createCell(0);
        cell.setCellValue("工程名稱");
        cell.setCellStyle(STYLE_HEADER);

        cell = row.createCell(1);
        cell.setCellValue(project.getProjectName());
        cell.setCellStyle(STYLE_CENTER);
        CellRangeAddress horizontalMerge = new CellRangeAddress(0, 0, 1, 3);
        sheet.addMergedRegion(horizontalMerge);

        cell = row.createCell(4);
        cell.setCellValue("業主合約");
        cell.setCellStyle(STYLE_HEADER);

        cell = row.createCell(5);
        cell.setCellValue(project.getAmount());
        cell.setCellStyle(STYLE_CENTER);
        horizontalMerge = new CellRangeAddress(0, 0, 5, 6);
        sheet.addMergedRegion(horizontalMerge);

        row = sheet.createRow(1);
        row.setHeightInPoints(20);

        cell = row.createCell(0);
        cell.setCellValue("施工地點");
        cell.setCellStyle(STYLE_HEADER);

        cell = row.createCell(1);
        cell.setCellValue(project.getConstructionSite());
        cell.setCellStyle(STYLE_CENTER);
        horizontalMerge = new CellRangeAddress(1, 1, 1, 3);
        sheet.addMergedRegion(horizontalMerge);

        cell = row.createCell(4);
        cell.setCellValue("工程編號");
        cell.setCellStyle(STYLE_HEADER);

        cell = row.createCell(5);
        cell.setCellValue(project.getProjectNumber());
        cell.setCellStyle(STYLE_CENTER);
        horizontalMerge = new CellRangeAddress(1, 1, 5, 6);
        sheet.addMergedRegion(horizontalMerge);
    }

    /**
     * 取得廠商項目不在業主項目中的
     */
    private Set<String> parseAppendItems(ReturnProjectAllData allData){
        // 專案項目
        Set<String> projectItemDescriptions = allData.getProjectItems().stream()
                .map(ReturnProjectItem::getItemDescription)
                .collect(Collectors.toSet());
        // 包商
        List<ReturnProjectContractor>  contractors =  allData.getContractors();
        // 包商合約項目
        Map<Integer, List<ReturnProjectContractorContractItem>> contractorContractItemMap = allData.getContractorContractItemMap();

        Set<String> appendItemDescriptions = new LinkedHashSet<>();
        for (ReturnProjectContractor contractor : contractors) {
            List<ReturnProjectContractorContractItem> contractItems = contractorContractItemMap.get(contractor.getId());
            System.out.println("contractItems.size:" + contractItems.size());
            for (ReturnProjectContractorContractItem obj : contractItems) {
                if (!projectItemDescriptions.contains(obj.getItemDescription())) {
                    appendItemDescriptions.add(obj.getItemDescription());
                }
            }
            projectItemDescriptions.addAll(appendItemDescriptions);
        }
        return appendItemDescriptions;
    }

    private void buildOwnerItems(Workbook workbook, Sheet sheet, List<ReturnProjectItem> items){
        Row row = sheet.createRow(2);
        row.setHeightInPoints(20);
        Cell cell;
        String[] headers = {"項 次", "項 目 及 說 明", "單 位", "數 量", "單 價", "複 價", "備 註"};
        for(int i = 0; i < headers.length; i++){
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(STYLE_HEADER);
        }
        for(int i = 0; i < items.size(); i++){
            row = sheet.createRow(i + 3);
            row.setHeightInPoints(20);
            ReturnProjectItem item = items.get(i);
            String[] itemBody = {
                    item.getItem(),
                    item.getItemDescription(),
                    item.getUnit(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getComplexPrice(),
                    item.getRemarks()};
            for(int j = 0; j < itemBody.length; j++){
                cell = row.createCell(j);
                cell.setCellValue(itemBody[j]);
                if (j == 0 || j == 2) {
                    cell.setCellStyle(STYLE_CENTER);
                } else if (j == 3 || j == 4 || j == 5) {
                    cell.setCellStyle(STYLE_RIGHT);
                }
            }
        }
    }

    private void buildAppendItemDescriptions(Sheet sheet, int projectItemSize, Set<String> appendItemDescriptions){
        int index = 0;
        Row row;
        Cell cell;
        for(String value: appendItemDescriptions){
            row = sheet.createRow((index++) + projectItemSize + 3);
            row.setHeightInPoints(20);
            cell = row.createCell(1);
            cell.setCellValue(value);
        }
    }
}
