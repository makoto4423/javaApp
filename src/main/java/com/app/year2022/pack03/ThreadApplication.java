package com.app.year2022.pack03;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadApplication {

    public static void main(String[] args){
        ThreadApplication app = new ThreadApplication();
        app.new Inner();
    }

    public class Inner{

    }

}
