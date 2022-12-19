package com.atguigu.nio.Selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class EX_25_NIOClient {
    public static void main(String[] args) {

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("\\localhost", 6666));

            while (!socketChannel.finishConnect()) {
                System.out.println("当前线程ID:"+Thread.currentThread().getId()+" 客户端连接服务器端,但是没有连接成功,继续连接");
            }
            String str = "hello! "+ Thread.currentThread().getId();
            socketChannel.write(ByteBuffer.wrap(str.getBytes()));
            System.out.println("当前线程ID:"+Thread.currentThread().getId()+" 客户端发送数据成功");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        }).start();
    }
}
