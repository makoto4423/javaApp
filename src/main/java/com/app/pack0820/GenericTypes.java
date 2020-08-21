package com.app.pack0820;

import java.util.ArrayList;
import java.util.List;

public class GenericTypes {

//    public static int method(List<Integer> list){
//        System.out.println("List<Integer> list");
//        return 1;
//    }

    public static String method(List<String> list){
        System.out.println("List<Integer> list");
        return "";
    }

    public static void main(String[] args){
        // method(new ArrayList<Integer>());
        method(new ArrayList<String>());
    }

}
