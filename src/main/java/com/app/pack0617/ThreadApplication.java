package com.app.pack0617;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThreadApplication {
    public static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<5;i++){
            MyThread thread = new MyThread(i);
            Future<?> future = executorService.submit(thread);
        }
        executorService.awaitTermination(300, TimeUnit.MILLISECONDS);
    }


    static class MyThread implements Runnable{

        int i;

        public MyThread(int i){
            this.i = i;
        }


        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread "+ i +" finished.");
        }
    }
}
