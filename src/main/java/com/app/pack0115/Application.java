package com.app.pack0115;

public class Application {

    public static void main(String[] args){
        String s  = "[[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]]";
        System.out.println(s.replaceAll("\\[","{").replaceAll("]","}"));
    }

}
