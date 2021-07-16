package com.app.year2021.month07;

import java.util.ServiceLoader;

public class SpiApplication {
    public static void main(String[] args){
        ServiceLoader<Human> loader = ServiceLoader.load(Human.class);
        for(Human h : loader){
            System.out.println(h.sex());
        }
    }
}
