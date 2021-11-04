package com.app.year2021.month09;

import org.apache.poi.ss.formula.functions.T;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LockApplication {
    static Map<Integer, Object> map = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread thread = new MyThread(i);
            thread.start();
        }
    }

    static class MyThread extends Thread {
        public int no;

        public MyThread(int no) {
            this.no = no;
        }

        @Override
        public void run() {
            Object object = map.computeIfAbsent(no % 10, k -> new Object());
            synchronized (object) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(no);
            }
        }
    }

}
