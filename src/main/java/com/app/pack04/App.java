package com.app.pack04;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) throws IOException {
        //为了简单起见，所有的异常信息都往外抛
        int port = 446;
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        //定义一个ServerSocket监听在端口8899上
        ServerSocket server = new ServerSocket(port);
        while (true) {
            Socket socket = server.accept();
            executorService.submit(() -> handler(socket));
        }
    }

    public static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            int len = 0;
            // http请求读到末尾后，inputStream的read会阻塞，socket不close的话，发送方也不会停止，直接close发送方也会报错(socket hang up,但停止了)
            // 要完整模拟，就需要解析出ContentLength(不往body放东西就没有这个属性)，正确的读完报文，然后再给出正确的http返回报文
            // 额，鸽了吧，只是看nio的时候偶然翻出这段代码，改了以前的错误写法而已。
//            while ((len = inputStream.read(bytes)) != -1) {
//                System.out.println(new String(bytes, 0, len));
//            }
            len = inputStream.read(bytes);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(bytes,0,len);
            outputStream.flush();
            System.out.println(new String(bytes, 0, len));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
