package com.app.year2021.month07;

import java.util.ArrayList;
import java.util.List;

public class StreamApplication {

    public static void main(String[] args){
        List<Man> list = new ArrayList<>();
        for(int i=0;i < 3; i++){
            Man man = new Man();
            man.name = i+"";
            list.add(man);
        }
        list.stream().findFirst().get().name = "undefined";
    }

}
