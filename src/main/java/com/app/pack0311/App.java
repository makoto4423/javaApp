package com.app.pack0311;

import java.io.*;

public class App {

    public static void main(String[] args) throws IOException {
        String path = "C:\\Users\\lawli\\Desktop\\2.txt";
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line;
        while ((line = reader.readLine()) != null) {
            String id = line.split("\t")[1];
            id = id.substring(1, id.length() - 1);
            String ql = line.split("\t")[0];
            ql = ql.substring(1, ql.length() - 1);
            ql = ql.replaceAll("'", "\"");
            ql = ql.replaceAll("\"\"", "\"");
            int index = ql.indexOf("{");
            while (index != -1) {
                ql = ql.substring(0, index + 1) + "service_name=\"$service_name\",scene_name=\"$scene_name\"," + ql.substring(index + 1);
                index = ql.indexOf("{", index + 1);
            }
            System.out.println("update mnt_panel_dimension t set t.prom_ql = '" + ql + "' where t.id = '" + id + "';");
        }
    }

}
