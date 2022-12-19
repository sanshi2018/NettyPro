package com.atguigu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {

        // 创建一个线程池
        // 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)

        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");
        // 循环监听等待客户端连接
        while(true) {
            // 监听，等待客户端连接
            System.out.println("线程信息 id = " + Thread.currentThread().getId() + " 名字 = " + Thread.currentThread().getName());
            System.out.println("等待连接...");
            // 这里会堵塞
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            // 如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
            newCachedThreadPool.execute(new Runnable() {
                public void run() { // 我们重写
                    // 可以和客户端通讯
                    handler(socket);
                }
            });
        }

    }
    // 编写一个handler方法，和客户端通讯
    public static void handler(Socket socket) {
        System.out.println("线程信息 id = " + Thread.currentThread().getId() + " 名字 = " + Thread.currentThread().getName());
        System.out.println("socket = " + socket);
        try {
            byte[] bytes = new byte[1024];
            // 通过socket获取输入流
            System.out.println("准备读取...");
            InputStream inputStream = socket.getInputStream();
            // 循环的读取客户端发送的数据
            while(true) {
                System.out.println("read IN Port"+socket.getPort());
                int read = inputStream.read(bytes);
                if(read != -1) { // 有数据
                    System.out.println(new String(bytes, 0, read)); // 输出客户端发送的数据
                } else {
                    break;
                }
            }
//            int read = socket.getInputStream().read(bytes);
//            if (read != -1) {
//                System.out.println(new String(bytes, 0, read)); // 输出客户端发送的数据
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
