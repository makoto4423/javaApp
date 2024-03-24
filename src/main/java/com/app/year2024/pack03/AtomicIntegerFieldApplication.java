package com.app.year2024.pack03;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldApplication {

    public static void main(String[] args) {
        FieldUpdaterTest test = new FieldUpdaterTest();
        System.out.println(ClassLayout.parseInstance(test).toPrintable());

        AtomicTest test2 = new AtomicTest();
        System.out.println(ClassLayout.parseInstance(test2).toPrintable());


    }

    public static class FieldUpdaterTest {

        public volatile int a = 0;
        public static final AtomicIntegerFieldUpdater<FieldUpdaterTest> updater = AtomicIntegerFieldUpdater.newUpdater(FieldUpdaterTest.class, "a");
    }

    public static class AtomicTest {

        public static final AtomicInteger atomicInteger = new AtomicInteger(0);
    }


}
