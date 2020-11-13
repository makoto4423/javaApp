package com.app.po;

public class John implements Man, Engineer,AutoCloseable {
    public void program() {
        System.out.println(" program ...");
    }

    public void live() {
        System.out.println(" live ... ");
    }

    public void cat(String c) {
        System.out.println(" cat " + c);
    }

    protected void test(){

    }

    @Override
    public void close() {

    }
}
