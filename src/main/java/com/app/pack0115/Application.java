package com.app.pack0115;

public class Application {

    public static void main(String[] args){
        String s  = "[[1,3,5,7],[10,11,16,20],[23,30,34,60]]";
        System.out.println(s.replaceAll("\\[","{").replaceAll("]","}"));
    }

}
