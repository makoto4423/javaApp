package com.app.year2022.pack12;

import org.apache.poi.xwpf.usermodel.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DocxApplication {

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        Path path = Paths.get("D:\\ats-2.docx");
        Files.deleteIfExists(path);
        try (FileOutputStream out = new FileOutputStream(path.toFile());
             XWPFDocument document = new XWPFDocument()) {
            mysql(document);
            document.write(out);
        }
    }

    public static void mysql(XWPFDocument document) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://10.8.6.125:3306/information_schema?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                "root", "root");
             Statement stmt = connection.createStatement()) {
            String[] tables = {"mnt_jvminfo", "mnt_jvminfo_his", "mnt_cpu_baseinfo",
                    "mnt_cpu_baseinfo_his", "mnt_cpu_coreinfo", "mnt_cpu_coreinfo_his",
                    "mnt_mem_info", "mnt_mem_info_his", "mnt_filesystem_info",
                    "mnt_filesystem_info_his", "mnt_network_info", "mnt_network_info_his",
                    "mnt_disk_info", "mnt_disk_info_his", "mnt_trade_info", "mnt_trade_info_his",
                    "mnt_busythread_info", "mnt_busythread_info_his", "mnt_svc_info", "mnt_svc_info_his",
                    "mnt_storage_info", "mnt_storage_info_his", "mnt_tran_stat_detail",
                    "mnt_tran_stat_detail_his", "mnt_global_node", "mnt_global_node_his",
                    "mnt_global_node_branch", "mnt_global_node_branch_his", "mnt_transaction_stats",
                    "mnt_transaction_stats_his", "opr_cluster", "opr_instance", "mnt_kpi_defined"};
            String[] zh = {"jvm汇报信息", "jvm汇报信息历史表", "cpu基础汇报信息", "cpu基础汇报信息历史表",
                    "cpu核心汇报信息", "cpu核心汇报信息历史表", "内存汇报信息", "内存汇报信息历史表", "文件系统汇报信息",
                    "文件系统汇报信息历史表", "网络汇报信息", "网络汇报信息历史表", "磁盘汇报信息", "磁盘汇报信息历史表",
                    "交易量信息", "交易量信息历史表", "繁忙线程信息", "繁忙线程信息历史表", "业务处理器信息",
                    "业务处理器信息历史表", "交换空间信息", "交换空间信息历史表", "事务耗时信息", "事务耗时信息历史表",
                    "全局节点", "全局节点历史表", "全局节点分支", "全局节点分支历史表", "事务状态信息", "事务状态信息历史表",
                    "集群表", "节点表", "指标定义表"
            };
            for (int i = 0; i < tables.length; i++) {
                mysql(document, stmt, tables[i], zh[i]);
            }
        }
    }

    public static void mysql(XWPFDocument document, Statement stmt, String t, String zh) throws SQLException {
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        run.addCarriageReturn();
        run.addCarriageReturn();
        run.addCarriageReturn();
        run.setText(" 表 " + t + "(" + zh + ")");
        XWPFTable table = document.createTable();
        XWPFTableRow headers = table.getRow(0);
        XWPFTableCell cell = headers.getCell(0);
        String[] h = new String[]{"编号", "名称", "数据类型", "长度", "小数位", "允许空值", "主键", "默认值", "说明", "是否索引字段", "索引名"};
        for (int i = 0; i < h.length; i++) {
            String s = h[i];
            cell.setText(s);
            if (i != h.length - 1) {
                cell = headers.createCell();
            }
        }
        int i = 0;
        ResultSet rs = stmt.executeQuery(
                "SELECT\n" +
                        "\tc.COLUMN_NAME COLUMN_NAME,\n" +
                        "\tc.DATA_TYPE DATA_TYPE,\n" +
                        "\tc.CHARACTER_MAXIMUM_LENGTH CHARACTER_MAXIMUM_LENGTH,\n" +
                        "\tc.IS_NULLABLE IS_NULLABLE,\n" +
                        "\tc.COLUMN_KEY COLUMN_KEY,\n" +
                        "\tc.COLUMN_COMMENT  COLUMN_COMMENT\n" +
                        "FROM\n" +
                        "\t`COLUMNS` c \n" +
                        "WHERE\n" +
                        "\tc.TABLE_SCHEMA = 'aweb4.4' \n" +
                        "\tAND c.TABLE_NAME = '" + t + "';"
        );
        while (rs.next()) {
            XWPFTableRow row = table.createRow();
            cell = row.getCell(0);
            cell.setText(i++ + "");
            String columnName = rs.getString("COLUMN_NAME");
            cell = row.getCell(1);
            cell.setText(columnName);
            String dataType = rs.getString("DATA_TYPE");
            cell = row.getCell(2);
            cell.setText(dataType);
            int length = rs.getInt("CHARACTER_MAXIMUM_LENGTH");
            cell = row.getCell(3);
            cell.setText(length + "");
            cell = row.getCell(4);
            cell.setText(0 + "");
            String isNull = rs.getString("IS_NULLABLE");
            cell = row.getCell(5);
            cell.setText("YES".equals(isNull) ? "Y" : "N");
            String key = rs.getString("COLUMN_KEY");
            cell = row.getCell(6);
            cell.setText("PRI".equals(key) ? "Y" : "N");
            String comment = rs.getString("COLUMN_COMMENT");
            cell = row.getCell(8);
            cell.setText(comment);
            cell = row.getCell(9);
            cell.setText("N");
            System.out.println(columnName + "  " + dataType + " " + length + " " + isNull + " " + key + " " + comment);
        }
    }


}
