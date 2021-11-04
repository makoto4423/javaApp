package com.app.year2021.month08;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintApplication {

    public static void main(String[] args) {
//        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8"};
//        List<String> list = Arrays.asList(arr);
//        for (int i = 1; i < 9; i++) {
//            System.out.println(bisectFind(list, i + ""));
//        }
        System.out.println("absent(agree_afa_engine_lsr_port_running{ip=\"10.8.4.89\", port=\"7882\"})==1");
    }


    public static int bisectFind(List<String> list, String target) {
        int l = 0, r = list.size() - 1;
        while (l < r) {
            int mid = (r - l) / 2 + l;
            if (list.get(mid).compareTo(target) < 0) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

}
