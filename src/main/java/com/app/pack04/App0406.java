package com.app.pack04;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.*;
import java.util.Objects;

public class App0406 {

    public static void main(String[] args) throws IOException {
        String path = "D:\\Chrome\\";
        // String[] catalogs = {"monitorClusterJson"};
        String[] catalogs = {"dashBoard2021-09-01.json"};
        for (String catalog : catalogs) {
            File file = new File(path + catalog);
            File[] files = file.listFiles();
            if(files != null){
                for (File f : files) {
                    if (f.isDirectory()) {
                        for (File k : Objects.requireNonNull(f.listFiles())) {
                            String s = loadFile(k);
                            replaceFile(s, k);
                        }
                    } else {
                        String s = loadFile(f);
                        replaceFile(s, f);
                    }
                }
            }
        }

    }


    public static String loadFile(File f) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f.getAbsolutePath()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }

    public static void replaceFile(String s, File file) throws IOException {
        boolean show = false;
        JSONObject object = JSONObject.parseObject(s);
        object.remove("panelID");
        object.remove("step");
        JSONObject panel = object.getJSONObject("panel");
        panel.remove("createTime");
        panel.remove("createUserId");
        panel.remove("createUserName");
        panel.remove("updateTime");
        panel.remove("updateUserId");
        panel.remove("updateUserName");
        panel.remove("dashboard");
        JSONObject dashboard = new JSONObject();
        dashboard.put("cluID", "");
        panel.put("dashboard", dashboard);
        panel.remove("style");
        panel.remove("layout");
        JSONArray dimensions = panel.getJSONArray("dimension");
        for (int i = 0; i < dimensions.size(); i++) {
            JSONObject dimension = dimensions.getJSONObject(i);
            dimension.remove("createTime");
            dimension.remove("createUserId");
            dimension.remove("createUserName");
            dimension.remove("updateTime");
            dimension.remove("updateUserId");
            dimension.remove("updateUserName");
            dimension.remove("panel");
            dimension.remove("index");
            dimension.remove("aggOp");
            dimension.remove("aggTarget");
            dimension.remove("filters");
            if(dimension.getString("promQL").contains("rate") ||
                    dimension.getString("promQL").contains("delta")){
                show = true;
            }
            JSONArray panelTable = dimension.getJSONArray("panelTable");
            if (panelTable != null) {
                for (int j = 0; j < panelTable.size(); j++) {
                    JSONObject table = panelTable.getJSONObject(j);
                    table.remove("dimension");
                    table.remove("panel");
                }
            }
        }
        if(show){
            System.out.println(file.getAbsolutePath());
            // System.out.println(object.toString(SerializerFeature.PrettyFormat));
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }

//        FileOutputStream fos = new FileOutputStream(file);
//        PrintWriter pw = new PrintWriter(fos);
//        pw.write(object.toString(SerializerFeature.PrettyFormat));
//        pw.close();
//        fos.close();
    }

}
