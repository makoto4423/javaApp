package com.app.pack0617;

import java.util.HashMap;
import java.util.Map;

public class CastApplication {

    public static void main(String[] args){
        Map<String,Object> map = new HashMap<>();
        map.put("a",1);
        int i = 2;
        map.put("b",i);
        double d = 0.2;
        map.put("c",d);
        map.put("d","string");
        for(Object o : map.values()){
            if(o instanceof Integer){
                System.out.println(((Integer) o).intValue());
            }else if(o instanceof Double){
                System.out.println(o);
            }else if(o instanceof String){
                System.out.println(o);
            }
        }
    }

}
