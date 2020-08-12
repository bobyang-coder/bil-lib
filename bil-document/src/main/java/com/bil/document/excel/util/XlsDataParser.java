package com.bil.document.excel.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;

/**
 * 利用poi读取Excel为字符串，(xls、xlsx通用)
 *
 * @author bob
 * @version 1.0
 * @since 2016/11/7
 */
public class XlsDataParser {
    private static final Logger logger = LoggerFactory.getLogger(XlsDataParser.class);

    private XlsDataParser() {
    }

    /**
     * 将Excel解析成字符串
     *
     * @param wb        工作薄
     * @param sheetNum  读取的sheet页码
     * @param startrows 读取开始行
     * @param rows      读取行数
     * @param startCols 读取开始列
     * @param cols      读取列数
     * @param readType  读取方式(取值[row：按行读，col：按列读])
     * @return
     */
    public static String parseExcel2String(Workbook wb, int sheetNum, int startrows, int rows, int startCols, int cols, String readType) {
        String str = "";
        try {
            if ("ROW".equals(readType) || "row".equals(readType)) {
                str = XlsDataParser.parseExcelByRow2String(wb, sheetNum, startrows, rows, startCols, cols);
            } else if ("COL".equals(readType) || "col".equals(readType)) {
                str = XlsDataParser.parseExcelByCol2String(wb, sheetNum, startCols, cols, startrows, rows);
            }
            logger.info("解析Excel数据为str======>>>" + str);
        } catch (Exception e) {
            logger.error("解析Excel数据异常", e);
        }
        return str;
    }

    /**
     * Excel解析（按行解析）
     *
     * @param rwb       工作薄
     * @param sheetNum  读取的sheet页码
     * @param startrows 读取开始行
     * @param rows      读取行数
     * @param startCols 读取开始列
     * @param cols      读取列数
     */
    public static String parseExcelByRow2String(Workbook rwb, int sheetNum, int startrows, int rows, int startCols, int cols) throws Exception {
        Sheet sheet = rwb.getSheetAt(sheetNum);
        StringBuilder sb = new StringBuilder();
        //遍历行
        for (int i = startrows; i < startrows + rows; ++i) {
            Row row = sheet.getRow(i);
            //遍历列
            for (int j = startCols; j < startCols + cols; ++j) {
                String cellStr = getCellValue(row.getCell(j));
                sb.append(cellStr.trim().replace(",", "")).append(",");
            }
            sb.append("0|");
        }
        return sb.toString();
    }

    /**
     * Excel解析（按列解析）
     *
     * @param rwb       工作薄
     * @param sheetNum  读取的sheet页码
     * @param startCols 读取开始列
     * @param cols      读取列数
     * @param startrows 读取开始行
     * @param rows      读取行数
     */
    public static String parseExcelByCol2String(Workbook rwb, int sheetNum, int startCols, int cols, int startrows, int rows) throws Exception {
        Sheet sheet = rwb.getSheetAt(sheetNum);
        StringBuilder sb = new StringBuilder();
        //遍历列
        for (int j = startCols; j < startCols + cols; ++j) {
            for (int i = startrows; i < startrows + rows; ++i) {
                String cellStr = getCellValue(sheet.getRow(i).getCell(j));
                sb.append(cellStr.trim().replace(",", "")).append(",");
            }
            sb.append("0|");
        }
        return sb.toString();
    }

//    //得到文件,得到文件类型
//    public Map<String, Map<String, String>> combinedStringList(InputStream inputStream) throws Exception {
//        Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
//        Workbook rwb = WorkbookFactory.create(inputStream);
//        int sheetCount = rwb.getNumberOfSheets();
//        //遍历sheet页
//        for (int i = 0; i < sheetCount; i++) {
//            Map<String, String> sheetContent = new HashMap<String, String>();
//            Sheet sheet = rwb.getSheetAt(i);
//            StringBuffer sb = new StringBuffer();
//            //遍历行
//            for (int j = 0; j < getLineCount(sheet); j++) {
//                Row row = sheet.getRow(j);
//                Iterator<Cell> cellIterator = row.cellIterator();
//                sb.append(j);
//                //遍历列
//                while (cellIterator.hasNext()) {
//                    sb.append("|");
//                    String cv = getCellValue(cellIterator.next());
//                    sb.append(cv);
//                }
//                sheetContent.put(String.valueOf(j), sb.toString());
//                sb.delete(0, sb.length());
//            }
//            map.put(String.valueOf(i), sheetContent);
//        }
//        inputStream.close();
//        return map;
//    }

    /**
     * 获取单元格数据内容,返回字符串
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    public static String getCellValue(Cell cell) {
        String strCell = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                strCell = cell.getRichStringCellValue().toString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                DecimalFormat df = new DecimalFormat("");
                strCell = df.format(cell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            case Cell.CELL_TYPE_FORMULA://公式
                try {
                    strCell = cell.getStringCellValue();
                } catch (IllegalStateException e) {
                    strCell = String.valueOf(cell.getNumericCellValue());
                }
                break;
            default:
                strCell = "";
                break;
        }
        return strCell.trim();
    }


    /**
     * 获取sheet页的有效行
     * 注意：空行以下不算有效
     *
     * @param sheet
     * @return
     */
    public static int getLineCount(Sheet sheet) {
        int line = 0;
        if (null != sheet) {
            int rowCount = sheet.getLastRowNum();//获取有效的行数
            for (int i = 0; i <= rowCount; i++) {//遍历行
                if (isBlankRow(sheet.getRow(i))) {
                    break;
                }
                line++;
            }
        }
        return line;
    }

    /**
     * 判断是否为空行
     *
     * @param row
     * @return
     */
    public static boolean isBlankRow(Row row) {
        if (row != null) {
            int cellCount = row.getPhysicalNumberOfCells();
            if (cellCount > 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] ddd) throws Exception {
        //FileInputStream stream = new FileInputStream(new File("C:\\Users\\Administrator\\Desktop\\9月人行报表\\表1-11支付机构客户资金账户余额变动调节表.xls"));
        //String s = XlsDataParser.parseExcel2String(stream, 0, 3, 29, 2, 30, "col");

        DecimalFormat df = new DecimalFormat("");
        String s = df.format(12.2334455);

        System.out.println("--------------" + s);
    }
}
