package com.app.pack04;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class App0408 {

    public static void main(String[] args) throws ParseException {
//        String s = "2021-04-08T02:20:54.234Z";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        Date date = sdf.parse(s);
//        System.out.println(sdf.format(date));

//        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        String str = df.format(date);
//        System.out.println(str);
//        date = df.parse(str);
//        str = df.format(date);
//        System.out.println(str);

        // String promQL = "node_uname_info{origin_prometheus=~\"$origin_prometheus\",job=~\"$job\"} - 0";
        String promQL = "sum(time() - node_boot_time_seconds{origin_prometheus=~\"$origin_prometheus\",job=~\"$job\"})by(instance)";
        while (promQL.contains("{")) {
            int left = promQL.indexOf("{");
            int right = promQL.indexOf("}", left);
            promQL = promQL.substring(0, left) + promQL.substring(right+1);
        }
        System.out.println(promQL);
    }

}
