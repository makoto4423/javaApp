package com.app.pack1219;

import java.util.Arrays;

public class Application {

    public static void main(String[] args){
        String s = "[0,1],[0,2],[0,3],[1,2]";
        System.out.println(s.replaceAll("\\[","{").replaceAll("]","}"));
    }


}
