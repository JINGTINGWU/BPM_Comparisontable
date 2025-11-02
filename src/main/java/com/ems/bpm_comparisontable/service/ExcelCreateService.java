package com.ems.bpm_comparisontable.service;

import com.ems.bpm_comparisontable.pojos.ReturnProjectAllData;
import com.ems.bpm_comparisontable.pojos.ReturnProjectMain;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Optional;

@Service
public class ExcelCreateService {
    @Autowired
    private ProjectDataService projectDataService;

    public ByteArrayOutputStream generateExcel(String projectName) throws Exception {
        // 建立工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("專案報告");

        Optional<ReturnProjectAllData> optReturnProjectAllData = projectDataService.getProjectAllData(projectName);
        if(optReturnProjectAllData.isPresent()) {
            ReturnProjectAllData  returnProjectAllData = optReturnProjectAllData.get();
            ReturnProjectMain project  = returnProjectAllData.getProject();

            Row row0 = sheet.createRow(0);

            /*
            // 建立標題列
            Row headerRow = sheet.createRow(0);
            String[] headers = {"序號", "專案名稱", "項目", "狀態", "建立時間"};

            // 設定標題樣式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 加入範例資料
            for (int i = 1; i <= 10; i++) {
                Row row = sheet.createRow(i);
                row.createCell(0).setCellValue(i);
                row.createCell(1).setCellValue(projectName);
                row.createCell(2).setCellValue("項目 " + i);
                row.createCell(3).setCellValue("進行中");
                row.createCell(4).setCellValue(new Date().toString());
            }





            // 自動調整欄寬
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

             */
        } else {

        }







        // 寫入到 ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream;
    }
}
