package com.app.pack0804;

/**
 * -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
 */
public class TestAllocation {

    private static final int _1MB  = 1024*1024;

    public static void main(String[] args){
        String s = "11111111111111111111111111111101";
        int res = 0;
        for(char ch : s.toCharArray()){
            res += (Integer.parseInt(ch+"") & 1);
            res = res<<1;
        }
        System.out.println(res);
    }

}
