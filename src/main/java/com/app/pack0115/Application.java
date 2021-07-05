package com.app.pack0115;

public class Application {

    public static void main(String[] args){
        String s  = "[0,2],[2,1],[3,4],[2,3],[1,4],[2,0],[0,4]";
        System.out.println(s.replaceAll("\\[","{").replaceAll("]","}"));
    }

}
