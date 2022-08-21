package com.app.year2021.month12;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileApplication {

    public static void main(String[] args) throws IOException {
//        loadFile("src/main/java/com/app/year2021/month12/nginx.conf");
//        writeFile("src/main/java/com/app/year2021/month12/nginx.conf","abc".getBytes());
        String s = "C:\\Users\\lawli\\Desktop\\what";
        rename(s);
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


    private static File createFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) return file;
        File parent = file.getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        file.createNewFile();
        return file;
    }

    private static void rename(String path){
        File f = new File(path);
        if(f.exists()){
            f.renameTo(new File(path+"-bak"));
        }
    }

}
