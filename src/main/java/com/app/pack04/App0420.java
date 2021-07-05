package com.app.pack04;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App0420 {

    public static void main(String[] args){
        String pattern = "T-[a-zA-Z0-9_-]+_[A-Z0-9]+[_[A-Z0-9]+]?";
        pattern = "T-[a-zA-Z0-9_-]+_[A-Z0-9]+(_[A-Z0-9]+)?$";
        Pattern p = Pattern.compile(pattern);
//        System.out.println(p.matcher("T-jkA_1B_A").matches());
//        System.out.println(p.matcher("T-jkA_1B_").matches());
//        System.out.println(p.matcher("T-_9").matches());
        // System.out.println(p.matcher("T-jkA_1B_A").matches());
        pattern = "C(-[a-zA-Z0-9_]+)(-[A-Z0-9_]+)(-.*)?$";
        p = Pattern.compile(pattern);
        Matcher matcher = p.matcher("C-DBANK-SERVICE-A");
        if(matcher.find()){
            // System.out.println(matcher.group(1));
        }
        pattern = "(javascript:|vbscript:|view-source:)+";
        p = Pattern.compile(pattern);
        System.out.println(p.matcher("javascript").matches());
    }

}
