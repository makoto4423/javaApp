package com.app.year2022.pack01;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReflectApplication {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Map<String,String> map = new HashMap<>();
        map.put("abc","def");
        Map<String, String> unmodifiableMap = Collections.unmodifiableMap(map);
        Field field = unmodifiableMap.getClass().getDeclaredField("m");
        field.setAccessible(true);
        Object obj = field.get(unmodifiableMap);
        Map<String,String> now = (Map<String, String>) obj;
        now.put("abc","eee");
        System.out.println(unmodifiableMap.values());
    }

}
