package com.app.pack0805;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Application {

    public static void createBusyThread(){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                while (true){

                }
            }
        },"busyThread");
        thread.start();
    }


    public static void createLockThread(final Object lock){
        Thread thread = new Thread(new Runnable() {
            public void run() {
                synchronized (lock){
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        },"lockThread");
        thread.start();
    }

    public static void main(String[] args) throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        reader.readLine();
//        createBusyThread();
//        reader.readLine();
//        createLockThread(new Object());
        for(int i=0;i<100;i++){
            new Thread(new SyncAddRunnable(1,2)).start();
            new Thread(new SyncAddRunnable(2,1)).start();
        }
    }

    static class SyncAddRunnable implements Runnable{

        int a,b;

        public SyncAddRunnable(int a, int b){
            this.a = a;
            this.b = b;
        }

        public void run() {
            synchronized (Integer.valueOf(a)){
                synchronized (Integer.valueOf(b)){
                    System.out.println(a+","+b);
                }
            }
        }
    }

}
