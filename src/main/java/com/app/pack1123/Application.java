package com.app.pack1123;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.sql.Types.NUMERIC;
import static org.apache.xmlbeans.impl.piccolo.xml.Piccolo.STRING;

public class Application {
    private static FormulaEvaluator evaluator;

    public static void main(String[] args) {
        try {
            String path = "C:\\Users\\lawli\\Documents\\WXWork\\1688852038467602\\Cache\\File\\2021-03\\1.xlsx";
            // 读取的时候可以使用流，也可以直接使用文件名
            FileInputStream is = new FileInputStream(path);
            String pvID = "";
            Set<String> set = new HashSet<>();
            Workbook xwb = new XSSFWorkbook(is);
            // 循环工作表sheet
            StringBuilder head = new StringBuilder("INSERT INTO `mnt_index_defined` (`ID`, `NAME`, `TYPE`, `LEVEL_1`, `LEVEL_1_NAME`, `LEVEL_2`, `LEVEL_2_NAME`, `LEVEL_3`, `LEVEL_3_NAME`, `LEVEL_4`, `LEVEL_4_NAME`, `LABELS`, `LABELS_NAME`, `UNIT_VALUE`,`PV_ID`) ");
            for (int numSheet = 0; numSheet < xwb.getNumberOfSheets(); numSheet++) {
                Sheet sheet = xwb.getSheetAt(numSheet);
                if (sheet == null) {
                    continue;
                }
                Union union = new Union();
                List<CellRangeAddress> range = sheet.getMergedRegions();
                range.removeIf(o -> o.getFirstColumn() != 5);
                range.forEach(o->union.union(o.getFirstRow(),o.getLastRow()));
                Map<String, String> map = new HashMap<>();
                map.put("instance", "节点");
                for (int numRow = 1; numRow <= sheet.getLastRowNum(); numRow++) {
                    Row row = sheet.getRow(numRow);
                    String key = getValue(row.getCell(6));
                    if (key == null || key.length() == 0) break;
                    String value = getValue(row.getCell(7));
                    map.put(key, value);
                }
                StringBuilder h1 = new StringBuilder("INSERT INTO `mnt_index_label` (`id`, `name`, `zh_name`) ");
                for(String k : map.keySet()){
                    StringBuilder dd = new StringBuilder(h1);
                    dd.append(" VALUES ('proIndex-").append(UUID.randomUUID().toString().replaceAll("-", "").substring(20)).append("',");
                    dd.append("'").append(k).append("',");
                    dd.append("'").append(map.get(k)).append("');");
                    System.out.println(dd);
                }
                String index1 = "", index2 = "", index3 = "", index4 = "", label = "";
                // 循环row，如果第一行是字段，则 numRow = 1
                for (int numRow = 1; numRow <= sheet.getLastRowNum(); numRow++) {
                    Row row = sheet.getRow(numRow);
                    if (row == null) {
                        continue;
                    }
                    StringBuilder sb = new StringBuilder(head);
                    sb.append("  VALUES (");
                    String value = getValue(row.getCell(0));
                    if (value == null) continue;
                    sb.append("'proIndex-").append(UUID.randomUUID().toString().replaceAll("-", "").substring(20)).append("',");
                    value = getValue(row.getCell(4));
                    if (value == null || value.length() == 0) continue;
                    sb.append("'").append(value).append("','',");
                    value = getValue(row.getCell(0));
                    if (value.length() != 0 && !value.equals(index1)) {
                        index1 = value;
                        index2 = "";
                        index3 = "";
                        index4 = "";
                    }
                    value = getValue(row.getCell(1));
                    if (value.length() != 0 && !value.equals(index2)) {
                        index2 = value;
                        index3 = "";
                        index4 = "";
                    }
                    value = getValue(row.getCell(2));
                    if (value.length() != 0 && !value.equals(index3)) {
                        index3 = value;
                        index4 = "";
                    }
                    value = getValue(row.getCell(3));
                    if (value.length() != 0 && !value.equals(index4)) {
                        index4 = value;
                    }
                    sb.append(spell(index1));
                    sb.append(spell(index2));
                    sb.append(spell(index3));
                    sb.append(spell(index4));
                    value = getValue(row.getCell(5));
                    if (value.length() != 0) {
                        label = "instance," + value;
                    }
                    if(value.length() == 0 && union.arr[numRow] == numRow){
                        label = "instance";
                    }
                    JSONObject object = new JSONObject();
                    for (String s : label.split(",")) {
                        if (!map.containsKey(s)) {
                            set.add(s);
                        }
                        object.put(s, map.getOrDefault(s, ""));
                    }
                    sb.append("'").append(label).append("',");
                    sb.append("'").append(object.toJSONString()).append("',");
                    sb.append("'").append(getValue(row.getCell(8))).append("',");
                    sb.append("'").append(pvID).append("');");
                    // System.out.println(sb.toString());
                }

            }
//            StringBuilder sb = new StringBuilder();
//            set.forEach(s-> sb.append(s).append(","));
//            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String spell(String index) {
        index = index.replaceAll(" ", "");
        index = index.replaceAll("（", "(");
        index = index.replaceAll("）", ")");
        StringBuilder sb = new StringBuilder();
        if (index.length() != 0) {
            sb.append("'").append(index, 0, index.indexOf("("))
                    .append("','").append(index, index.indexOf("(") + 1, index.length() - 1).append("',");
        } else {
            sb.append("'','',");
        }
        return sb.toString();
    }

    public static String getValue(Cell cell) {
        String val = "";
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:   // 字符串类型
                val = cell.getStringCellValue().trim();
                break;
            case NUMERIC:  // 数值类型
                // 日期格式
                if (DateUtil.isCellDateFormatted(cell)) {
                    val = Date2Str(cell.getDateCellValue(), "yyyy-MM-dd HH:mm:ss");
                } else {
                    // 四舍五入
                    val = new DecimalFormat("#.####").format(cell.getNumericCellValue());
                }
                break;
            default: //其它类型
                break;
        }
        return val;
    }

    public static String Date2Str(Date date, String format) {
        // Date -> LocalDateTime -> String
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), zone);
        return df.format(localDateTime);
    }

    static class Union{
        int[] arr;

        public Union(){
            arr = new int[500];
            for(int i=0;i<200;i++){
                arr[i] = i;
            }
        }

        public void union(int less,int more){
            while (more != less){
                arr[more] = less;
                more--;
            }
        }
    }
}
