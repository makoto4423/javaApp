package com.app.pack0901;

import com.app.po.John;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Application {

    public static void main(String[] args){
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return false;
            }
        };
        Supplier<John> supplier = John::new;
        supplier.get();
        Consumer<John> consumer = John::live;
        consumer.accept(new John());

    }

}
