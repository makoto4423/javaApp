package com.app.pack0311;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//测试代码
public class DynamicProxy {

    public static void main(String[] args) {
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true"); //保存生成的动态代理.class文件

        try {
            TargetObject object = new TargetObject();
            InvocationHandler handler = new DynamicProxyHandler(object);
            Class<?> clazz = Proxy.getProxyClass(
                    TargetObject.class.getClassLoader(),
                    InterfaceB.class, InterfaceA.class); //注意：这里先写接口B，然后写接口A
            Constructor<?> constructor = clazz.getConstructor(InvocationHandler.class);
            InterfaceA proxy_a = (InterfaceA) constructor.newInstance(new Object[] {handler});
            InterfaceB proxy_b = (InterfaceB) constructor.newInstance(new Object[] {handler});

            proxy_a.doSomething();
            proxy_a.doA();
            proxy_b.doSomething();
            proxy_b.doB();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public interface InterfaceA {
        void doSomething();
        void doA();
    }

    public interface InterfaceB {
        void doSomething();
        void doB();
    }

    static class TargetObject implements InterfaceA, InterfaceB {
        public void doSomething() {
            System.out.println("target doSomething");
        }
        public void doA() {
            System.out.println("target doA");
        }
        public void doB() {
            System.out.println("target doB");
        }
    }

    static class DynamicProxyHandler implements InvocationHandler { //动态代理处理器
        private final Object target;
        public DynamicProxyHandler(Object target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("InvocationHandler: " + proxy.getClass() +
                    ", method: " + method + ", args: " + args);
            if(args != null) {
                for(Object arg : args) {
                    System.out.println("InvocationHandler args: " + arg);
                }
            }
            return method.invoke(target, args);
        }
    }

}


