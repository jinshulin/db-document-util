package com.jd.ips.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import com.jd.ips.entity.TableColumnDesc;
import com.jd.ips.entity.TableDesc;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @description  
 *
 * @author jinshulin(jinshulin@jd.com)
 * @since 2018年10月16日 09时23分
 */
public class ExcelUtil {

    public static void output(String path, List<TableDesc> list) throws IOException {
        File file = createFile(path);
        XSSFWorkbook wb = new XSSFWorkbook();
        XSSFSheet sheet = wb.createSheet();
        int rowNum = 0;
        for (TableDesc table : list) {
            rowNum = writeTable2File(sheet, table, rowNum);
            // 插入空行.
            rowNum ++;
        }
        workbookWrite(wb, file);
    }

    /**
     * workbook写入
     *
     * @param workbook
     * @param file
     * @throws IOException
     */
    private static void workbookWrite(Workbook workbook, File file) throws IOException {

        // 写流
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            workbook.write(out);
            out.flush();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw e;
                }
            }
        }
    }

    private static int writeTable2File(XSSFSheet sheet, TableDesc table, int rowNum) {
        rowNum = createTableDesc(sheet, table, rowNum);
        rowNum ++;
        rowNum = createColumnHeader(sheet, rowNum);
        rowNum ++;
        rowNum = createColumn(sheet, table.getTableColumnDesc(), rowNum);
        return rowNum;
    }

    private static int createColumn(XSSFSheet sheet, List<TableColumnDesc> list, int rowNum) {
        for (TableColumnDesc column : list) {
            XSSFRow row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(column.getName());
            row.createCell(1).setCellValue(column.getDataType());
            if (null == column.getDataLen()) {
                row.createCell(2).setCellValue("");
            } else {
                row.createCell(2).setCellValue(column.getDataLen());
            }
            if ("YES".equalsIgnoreCase(column.getNullable())) {
                row.createCell(3).setCellValue("是");
            } else {
                row.createCell(3).setCellValue("否");
            }
            if (null == column.getDesc()) {
                row.createCell(4).setCellValue("");
            } else {
                row.createCell(4).setCellValue(column.getDesc());
            }
            rowNum ++;
        }
        return rowNum;
    }

    private static int createColumnHeader(XSSFSheet sheet, int rowNum) {
        XSSFRow row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("列名");
        row.createCell(1).setCellValue("类型");
        row.createCell(2).setCellValue("长度");
        row.createCell(3).setCellValue("是否非空");
        row.createCell(4).setCellValue("描述");
        return rowNum;
    }

    private static int createTableDesc(XSSFSheet sheet, TableDesc table, int rowNum) {
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 2));
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 4, 5));

        sheet.autoSizeColumn(0, true);
        sheet.autoSizeColumn(1, true);
        sheet.autoSizeColumn(2, true);
        sheet.autoSizeColumn(3, true);
        sheet.autoSizeColumn(4, true);

        XSSFRow row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue("表名");
        row.createCell(1).setCellValue(table.getName());
        row.createCell(3).setCellValue("表描述");
        row.createCell(4).setCellValue(table.getDesc());
        return rowNum;
    }

    private static File createFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            FileUtils.forceDelete(file);
        }
        FileUtils.touch(file);
        return file;
    }
}
