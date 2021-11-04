package com.app.year2021.month10;

public class ExceptionApplication {

    public static void main(String[] args){
        try {
            exception(1);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            exception(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void exception(int i){
        try {
            throw new RuntimeException();
        } finally {
            if (i == 0)
                return;
        }
    }

}
