package com.app.year2022.pack12;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlApplication {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://10.8.6.125:3306/aweb4.4?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true",
                "root", "root")) {
            Statement stmt = null;
            try {
                List<String> list = new ArrayList<>();
                List<String> zh = new ArrayList<>();
                stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "select t.KPI_STORE_TABLE KPI_STORE_TABLE, t.KPI_CNAME KPI_CNAME from mnt_kpi_defined t "
                );
                while (rs.next()) {
                    list.add(rs.getString("KPI_STORE_TABLE"));
                    zh.add(rs.getString("KPI_CNAME"));
                }
                for(String s : list){
                    System.out.print("\""+s + "\",");
                    System.out.print("\""+s + "_his\",");
                }
                System.out.println();
                for(String s : zh){
                    System.out.print("\""+s + "\",");
                    System.out.print("\""+s + "历史表\",");
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
        }

    }

}
