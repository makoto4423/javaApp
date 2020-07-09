package com.app;

import com.app.po.Engineer;
import com.app.po.John;
import com.app.po.Man;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class application {

    public static void main(String[] args) {
        jdkProxy();
    }


    public static void cglib(){
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "D:\\temp");
        // John john = new John();
        MethodInterceptor interceptor = new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println(" invoke ...");
                return methodProxy.invokeSuper(o,objects);
            }
        };
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(interceptor);
        enhancer.setSuperclass(John.class);
        John john = (John) enhancer.create();
        john.live();
    }

    public static void jdkProxy(){
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        final John john = new John();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?>[] interfaces = john.getClass().getInterfaces();
        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(john);
            }
        };
        Object o = Proxy.newProxyInstance(loader, interfaces, handler);
        Man man = (Man) o;
        man.live();
        Engineer engineer = (Engineer) o;
        engineer.program();
        //generateClassFile(john.getClass(),"JohnProxy");
    }

    public static void generateClassFile(Class<?> clazz, String proxyName) {
        //根据类信息和提供的代理类名称，生成字节码
        byte[] classFile = ProxyGenerator.generateProxyClass(proxyName, clazz.getInterfaces());
        String paths = clazz.getResource(".").getPath();
        System.out.println(paths);
        FileOutputStream out = null;

        try {
            //保留到硬盘中
            out = new FileOutputStream(paths + proxyName + ".class");
            out.write(classFile);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
