package com.app.pack0810;

/**
 * 1.8上不存在初始化加锁的问题
 */
public class DeadLoopClass {

    public DeadLoopClass(){
        System.out.println(Thread.currentThread()+"init DeadLoopClass");
        while (true){

        }
    }

    public static void main(String[] args){
        Runnable script = new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread()+" start");
                DeadLoopClass dlc = new DeadLoopClass();
                System.out.println(Thread.currentThread()+" end");
            }
        };
        Thread t1 = new Thread(script);
        Thread t2 = new Thread(script);
        t1.start();
        t2.start();
    }

}
