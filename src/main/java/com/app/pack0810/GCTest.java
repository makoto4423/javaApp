package com.app.pack0810;

/**
 * -verbose:gc
 */
public class GCTest {

    public static void main(String[] args){
        {
            byte[] b = new byte[64*1024*1024];
        }
        int a = 0;
        System.gc();
    }

}
