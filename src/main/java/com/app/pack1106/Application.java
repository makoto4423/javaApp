package com.app.pack1106;

import com.app.po.John;
import com.app.po.Man;

import java.lang.annotation.Annotation;
import java.util.*;

public class Application {

    public static void main(String[] args){
        Annotation[] annotations = Man.class.getAnnotations();
        System.out.println(annotations.length);
        for(Annotation ano : annotations){
            System.out.println(ano.getClass());
            ManyAno manyAno = (ManyAno) ano;
            System.out.println(manyAno.value().length);
        }
        Object[] arr = new Long[1];
        union(new HashSet<Integer>(),new HashSet<Double>());
        Class<String> stringClass = (Class<String>) "dd".getClass();
        stringClass.cast("ddd");
    }

    private static  <E> Set<E> union(Set<? extends E> s1, Set<? extends E> s2){
        return new HashSet<>();
    }

    public void swap(List<?> list, int i,int j){
        swapHelp(list,i,j);
    }

    public <E> void swapHelp(List<E> list,int i,int j){
        list.set(i,list.get(j));
    }

}
