package com.jt.stock;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

/**
 * Created by he on 2016/5/11.
 */
public class ExcelTest {

    @Test
    public void test01() throws Exception {
        File file = new File("C:\\Users\\he\\Downloads\\2016-05-10.xls");

        try (FileInputStream fis = new FileInputStream(file)) {
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            System.out.println(workbook.sheetIterator().hasNext());
            HSSFSheet spreadsheet = workbook.getSheetAt(0);
            print(spreadsheet);
        }
    }



    @Test
    public void test03() throws Exception {
        File file = new File("C:\\Users\\he\\Downloads\\test.xls");

        Workbook sheets = WorkbookFactory.create(file);
        print(sheets.getSheetAt(0));
    }


    @Test
    public void test02() throws Exception {
        File file = new File("C:\\Users\\he\\Downloads\\2016-05-10.xls");
        try (FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook workbook = new XSSFWorkbook (fis);
            XSSFSheet spreadsheet = workbook.getSheetAt(0);
            print(spreadsheet);
        }
    }

    private void print(Sheet spreadsheet) {
        Iterator<Row> rowIterator = spreadsheet.iterator();
        while (rowIterator.hasNext())
        {
            Row row = rowIterator.next();
            Iterator <Cell> cellIterator = row.cellIterator();
            while ( cellIterator.hasNext())
            {
                Cell cell = cellIterator.next();
                switch (cell.getCellType())
                {
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(
                                cell.getNumericCellValue() + " \t\t " );
                        break;
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(
                                cell.getStringCellValue() + " \t\t " );
                        break;
                }
            }
            System.out.println();
        }
    }

}
