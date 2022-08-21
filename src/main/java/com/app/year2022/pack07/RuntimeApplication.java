package com.app.year2022.pack07;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class RuntimeApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(foolish());
    }

    public static String foolish() throws IOException, InterruptedException {
        //        StringTokenizer st = new StringTokenizer("db2cmd -c -w -i d.bat BFF db2inst1 xjxa12333 bff_entity,newtable .");
//        String[] cmd = new String[st.countTokens()];
//        for (int i = 0; st.hasMoreTokens(); i++)
//            cmd[i] = st.nextToken();
//        File out = new File("out1");
//        File err = new File("err");
        File out = File.createTempFile("out-","out");
        File err = File.createTempFile("err-","err");
        ProcessBuilder builder = new ProcessBuilder().command("java","-version");
//          .command("cmd","/c","mysqldump", "-h", "10.8.4.190", "-P", "3307", "-u", "root", "-p\"root\"",
//                  "--ignore-table=ds0.BFF_API", "ds0", ">", "bff.sql");
        builder.redirectOutput(out);
        builder.redirectError(err);
        AtomicBoolean b = new AtomicBoolean(true);
        Process process = builder.start();
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                if (process.isAlive()) {
                    process.destroyForcibly();
                    b.set(false);
                }
            }
        });
        thread.start();
        process.waitFor();

        {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(out.toPath()), StandardCharsets.UTF_8))) {
                char[] ch = new char[1024];
                StringBuilder sb = new StringBuilder("success");
                int i;
                while ((i = reader.read(ch)) != -1) {
                    sb.append(new String(ch, 0, i));
                }
                System.out.println(sb);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(err.toPath()), Charset.forName("GBK")))) {
            char[] ch = new char[1024];
            int i;
            StringBuilder sb = new StringBuilder("error");
            while ((i = reader.read(ch)) != -1) {
                sb.append(new String(ch, 0, i));
            }
            System.out.println(sb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return b.get() ? "ok" : "err";
    }


}

