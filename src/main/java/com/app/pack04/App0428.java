package com.app.pack04;

import org.springframework.util.StringUtils;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class App0428 {

    public static void main(String[] args) throws IOException {
        String path = "D:\\intellij\\javaApp\\src\\main\\java\\com\\app\\pack04\\";
        File f = new File(path + "auth");
        BufferedReader reader = new BufferedReader(new FileReader(f.getAbsolutePath()));
        String table = "";
        Set<String> set = new HashSet<>();
        Map<String, Set<String>> map = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isEmpty(line)) {
                map.put(table, set);
                continue;
            }
            if (line.charAt(0) != ' ') {
                table = line.trim();
                set = new HashSet<>();
            } else {
                line = line.substring(4);
                set.add(line.split(" ")[0]);
            }
        }
        reader.close();
        reader = new BufferedReader(new FileReader(path + "me"));
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isEmpty(line)) continue;
            if (line.charAt(0) != ' ') {
                table = line.trim();
            } else {
                line = line.substring(3);
                line = line.split(" ")[0];
                line = line.substring(0, line.length() - 1);
                line = line.toLowerCase();
                if (map.get(table).contains(line)) {
                    map.get(table).remove(line);
                } else {
                    System.out.println(table + "  " + line);
                }
            }
        }
        reader.close();
    }

}
