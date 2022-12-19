package com.atguigu.nio.Selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class EX_24_NIOServer {
    public static void main(String[] args) throws IOException {
        // 服务器方面开启的一个通道，用于监听客户端的连接
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // selector对象
        Selector selector = Selector.open();

        // 绑定端口到socket,并启动
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把serverSocketChannel注册到selector,关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        // 循环等待客户端连接
        while (true) {
            // 这里我们等待1秒,如果没有事件发生,返回
            if (selector.select(1000) == 0) { // 没有事件发生
                System.out.println("当前线程ID:"+Thread.currentThread().getId()+" 服务器等待了1秒,无连接");
                continue;
            }

            // 如果返回的>0,就获取到相关的selectionKey集合
            // 1.如果返回的>0,表示已经获取到关注的事件
            // 2.selector.selectedKeys()返回关注事件的集合
//             通过selectionKeys反向获取通道
             selector.selectedKeys().forEach(selectionKey -> {
                 // 如果是客户端来建立链接事件，需要注册进去
                 if (selectionKey.isAcceptable()) { // 如果是OP_ACCEPT,有新的客户端连接
                     // 给该客户端生成一个SocketChannel
                     try {
                         // 获取与客户端通信的Channel
                         SocketChannel socketChannel = serverSocketChannel.accept();
                         System.out.println("客户端连接成功,生成了一个socketChannel" + socketChannel.hashCode());
                         // 将socketChannel设置为非阻塞
                         socketChannel.configureBlocking(false);
                         // 将当前的socketChannel也注册到selector,关注事件为OP_READ,同时给socketChannel关联一个Buffer
                         socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1));
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }
                 // 处于 establish 的客户端发送数据过来
                 if (selectionKey.isReadable()) { // 发生OP_READ
                     // 通过key反向获取到对应的channel
                     SocketChannel channel = (SocketChannel) selectionKey.channel();
                     // 获取到该channel关联的buffer
                     ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                     try {
                         channel.read(buffer);
                     } catch (IOException e) {
                         throw new RuntimeException(e);
                     }
                     // 打印当前线程id
                     System.out.println("当前线程ID:"+Thread.currentThread().getId()+" from 客户端" + new String(buffer.array()));
                     // 读完之后，需要将buffer清空
                        buffer.clear();
                        // 关闭通道
//                     try {
//                         channel.close();
//                     } catch (IOException e) {
//                         throw new RuntimeException(e);
//                     }

                 }
                 // 手动从集合中移动当前的selectionKey,防止重复操作

                 selector.selectedKeys().remove(selectionKey);
             });
            // 睡眠3s
//            try {
//                System.out.println("睡眠ing");
//                Thread.sleep(3000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }


        }



    }
}
