package com.app.pack0820;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyTest {

    interface IHello{
        void sayHello();
        void sayHi();
    }

    static class Hello implements IHello{

        public void sayHello() {
            System.out.println("hello");
        }

        public void sayHi() {
            System.out.println("hi");
        }
    }

    static class DynamicProxy implements InvocationHandler{

        Object oriObj;

        Object bind(Object oriObj){
            this.oriObj = oriObj;
            return Proxy.newProxyInstance(oriObj.getClass().getClassLoader(), oriObj.getClass().getInterfaces(), this);
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("welcome");
            return method.invoke(oriObj, args);
        }
    }

    public static void main(String[] args){
        IHello hello = (IHello) new DynamicProxy().bind(new Hello());
        hello.sayHello();
    }

}
