package com.app.pack0115;

import org.springframework.util.Assert;

public class LCApplication {

    public static void main(String[] args){
        String s  = "[9,10,2],[4,5,6],[6,8,1],[1,5,5],[4,9,5],[1,6,5],[4,8,3],[4,7,10],[1,9,8],[2,3,5]";
        System.out.println(s.replaceAll("\\[","{").replaceAll("]","}"));

    }

}
