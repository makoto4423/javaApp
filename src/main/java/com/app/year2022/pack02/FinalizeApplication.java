package com.app.year2022.pack02;

public class FinalizeApplication {

    private static FinalizeApplication hook = null;

    public void isAlive(){
        System.out.println("alive");
    }

    public void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize execute...");
        hook = this;
    }

    public static void main(String[] args) throws Throwable {
        hook = new FinalizeApplication();
        hook = null;
        System.gc();
        Thread.sleep(5000);
        if(hook != null){
            hook.isAlive();
        }else{
            System.out.println("hook is null");
        }
        hook = null;
        System.gc();
        if(hook != null){
            hook.isAlive();
        }else{
            System.out.println("hook is null");
        }
    }


}
