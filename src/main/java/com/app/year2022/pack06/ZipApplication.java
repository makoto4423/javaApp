package com.app.year2022.pack06;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipApplication {

    public static void main(String[] args) throws IOException {
        String path = "D:\\wechat\\WeChat Files\\wxid_v48jxlygodox21\\FileStorage\\MsgAttach\\9e20f478899dc29eb19741386f9343c8\\File\\2022-07\\post.zip";
        String pwd = "";
        try (ZipFile zipFile = new ZipFile(path, Charset.forName("GBK"))) {
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = enumeration.nextElement();
                if (!entry.isDirectory()) {
//                    InputStream inputStream = zipFile.getInputStream(entry);
//                    int n;
//                    StringBuilder sb = new StringBuilder(entry.getName() + "--");
//                    char[] bytes = new char[1024];
//                    BufferedReader bufferedReader;
//                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
//                    while ((n = bufferedReader.read(bytes)) != -1) {
//                        sb.append(bytes, 0, n);
//                    }
                    System.out.println(entry.getName());
                }
            }

        }
    }

}
