package com.app.pack1124;

import org.springframework.util.NumberUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

public class Application {

    public static void main(String[] args) {
//        LocalDateTime time = LocalDateTime.now();
//        time.toInstant(ZoneOffset).toEpochMilli();
//        Set<String> set = new HashSet<>();
//        set.remove(null);
//        System.out.println("a"=="a");
//        List<String> list = new ArrayList<>();
//        list.stream();
//        Function<String,Integer> toInteger = (s)->Integer.valueOf(s);
//        String s = "{{abc}}";
//        s = s.replaceAll("\\{\\{","+");
//        s = s.replaceAll("}}","-");
//        char ch = 0;
//        System.out.println(Integer.MAX_VALUE);

//        int mask = 6;
//        int sub = mask;
//        do {
//            sub = (sub - 1) & mask;
//            // System.out.println(sub);
//        } while (sub != mask);
//        System.out.println(Integer.toBinaryString(-1));

        String s = "abc";
        int mask = 0;
        for(int i=1;i<s.length();i++){
            mask |= (1 << s.charAt(i)-'a');
        }
        mask |= (1);
        System.out.println("first mask");
        System.out.println("mask"+Integer.toBinaryString(mask));
        int sub = mask;
        do{
            System.out.println("sub"+Integer.toBinaryString(sub));
            sub = (sub - 1) & mask;
        }while (sub != mask);
        mask = 0;
        for(char ch : s.toCharArray()){
            mask |= (1 << ch-'a');
        }
        System.out.println("second mask");
        System.out.println("mask"+Integer.toBinaryString(mask));
        sub = mask;
        do{
            System.out.println("sub"+Integer.toBinaryString(sub));
            sub = (sub - 1) & mask;
        }while (sub != mask);

    }

}
