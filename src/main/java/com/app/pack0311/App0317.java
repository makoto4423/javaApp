package com.app.pack0311;

import java.util.Arrays;

public class App0317 {

    public static void main(String[] args){
//        int integer = 53;
//        System.out.println(Integer.hashCode(integer));
//        String s = "(sum(avg(node_filesystem_size_bytes{cluster_id=\"$cluster_id\",fstype=~\"xfs|ext.*\"})by(device,instance)) - sum(avg(node_filesystem_free_bytes{cluster_id=\"$cluster_id\",fstype=~\"xfs|ext.*\"})by(device,instance))) *100/(sum(avg(node_filesystem_avail_bytes{cluster_id=\"$cluster_id\",fstype=~\"xfs|ext.*\"})by(device,instance))+(sum(avg(node_filesystem_size_bytes{cluster_id=\"$cluster_id\",fstype=~\"xfs|ext.*\"})by(device,instance)) - sum(avg(node_filesystem_free_bytes{cluster_id=\"$cluster_id\",fstype=~\"xfs|ext.*\"})by(device,instance))))";
//        s = s.replaceAll("\\$"+"cluster_id","key");
//        System.out.println(s);
        String s = "a,b,v";
        System.out.println(Arrays.toString(s.split(",")));
    }

}
