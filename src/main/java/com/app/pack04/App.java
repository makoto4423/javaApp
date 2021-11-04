package com.app.pack04;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class App {

    public static void main(String[] args) throws IOException {
        //为了简单起见，所有的异常信息都往外抛
        int port = 444;
        //定义一个ServerSocket监听在端口8899上
        ServerSocket server = new ServerSocket(port);
        //server尝试接收其他Socket的连接请求，server的accept方法是阻塞式的
        Socket socket = server.accept();
        //跟客户端建立好连接之后，我们就可以获取socket的InputStream，并从中读取客户端发过来的信息了。
        Reader reader = new InputStreamReader(socket.getInputStream());
        char chars[] = new char[1024];
        int len;
        while (true){
            StringBuilder sb = new StringBuilder();
            reader.read(chars);
            sb.append(new String(chars));
            System.out.println("from client: " + sb);
            sb = new StringBuilder();
            socket.getOutputStream().write("over".getBytes());
            socket.getOutputStream().flush();
        }
    }

}
