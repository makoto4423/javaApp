package com.app.year2021.month12;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileApplication {

    public static void main(String[] args){
        loadFile("src/main/java/com/app/year2021/month12/nginx.conf");
        writeFile("src/main/java/com/app/year2021/month12/nginx.conf","abc".getBytes());
    }


    public static byte[] loadFile(String uri) {
        File file = new File(uri);
        try {
            long size = file.length();
            if(size > Integer.MAX_VALUE){
                throw new RuntimeException("文件过大");
            }
            System.out.println(file.getAbsolutePath());
            byte[] bytes = new byte[(int) file.length()];
            FileInputStream inputStream = new FileInputStream(file);
            inputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String writeFile(String uri, byte[] content) {
        try {
            FileOutputStream stream = new FileOutputStream(uri);
            stream.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }

}
