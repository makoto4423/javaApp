package com.app.year2024.pack03;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class IdentityHashMapApplication {

    public static void main(String[] args) {
        Integer a = new Integer(1);
        Integer b = new Integer(1);
        IdentityHashMap<Integer, String> identityHashMap = new IdentityHashMap<>();
        identityHashMap.put(a, "a");
        identityHashMap.put(b, "b");
        System.out.println(identityHashMap);
        Map<Integer, String> map = new HashMap<>();
        map.put(a, "a");
        map.put(b, "b");
        System.out.println(map);
        System.out.println(a==b);
        Eng i = new Eng();
        Eng j = new Eng();
        IdentityHashMap<Eng, String> engStringIdentityHashMap = new IdentityHashMap<>();
        engStringIdentityHashMap.put(i, "a");
        engStringIdentityHashMap.put(j, "b");
        Map<Eng, String> engStringMap = new HashMap<>();
        engStringMap.put(i, "a");
        engStringMap.put(j, "b");
        System.out.println(engStringIdentityHashMap);
        System.out.println(engStringMap);
    }

    static class Eng {
        @Override
        public int hashCode() {
            return 1;
        }
    }


}
