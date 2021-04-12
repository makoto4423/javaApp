package com.app.pack0107;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Application {

    public static volatile boolean flag = true;
    private static final String graFromDate = "\"${__from:date:";

    public static void main(String[] args) throws InterruptedException {
//        Thread thread = new Thread(() -> {
//            int i = 0;
//            while (flag) {
//                i++;
//            }
//        });
//        thread.start();
//        Thread.sleep(1000);
//        flag = false;
//        System.out.println("over");
//        System.out.println(~1);
//        Set<String> set = new HashSet<>();
//        set.remove(null);
        String q = "sum(agree_afa_engine_svc_scene_executed_total{date_time=\"${__from:date:yyyy-MM-dd}\",instance=\"10.8.4.190\"})";
        int left = q.indexOf(graFromDate);
        int right = q.indexOf("}\"");
        LocalDateTime fromDate;
        fromDate = getStartDefault();
        while (left != -1){
            String dateFormat = q.substring(left + graFromDate.length(), right);
            q = q.substring(0,left)+"\""+fromDate.format(DateTimeFormatter.ofPattern(dateFormat))+q.substring(right+1);
            left = q.indexOf(graFromDate);
            right = q.indexOf("}\"");
        }
        // q = q.replaceAll(graFromDate + dateFormat + "}\"", "\"" + time + "\"");
        System.out.println(q);
    }

    public static LocalDateTime getStartDefault() {
        return LocalDateTime.now().minusDays(1);
    }
}
