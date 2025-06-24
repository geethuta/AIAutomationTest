package com.testscript.generator.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {
    public static class TestCase {
        public String summary;
        public String steps;
        public TestCase(String summary, String steps) {
            this.summary = summary;
            this.steps = steps;
        }
    }

    public static List<TestCase> getTestCases(String excelPath) throws IOException {
        List<TestCase> testCases = new ArrayList<>();
        FileInputStream fis = new FileInputStream(new File(excelPath));
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        int summaryCol = -1, stepsCol = -1;
        if (rowIterator.hasNext()) {
            Row headerRow = rowIterator.next();
            for (Cell cell : headerRow) {
                String cellValue = cell.getStringCellValue().trim().toLowerCase();
                if (cellValue.equals("summary")) summaryCol = cell.getColumnIndex();
                if (cellValue.equals("steps")) stepsCol = cell.getColumnIndex();
            }
        }
        if (summaryCol == -1 || stepsCol == -1) {
            workbook.close();
            fis.close();
            throw new RuntimeException("Could not find 'summary' or 'steps' columns in Excel file");
        }
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Cell summaryCell = row.getCell(summaryCol);
            Cell stepsCell = row.getCell(stepsCol);
            String summary = summaryCell != null ? summaryCell.toString() : "";
            String steps = stepsCell != null ? stepsCell.toString() : "";
            testCases.add(new TestCase(summary, steps));
        }
        workbook.close();
        fis.close();
        return testCases;
    }
} 