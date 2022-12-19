package com.atguigu.nio.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class EX_32_GroupChatClient {
    // 定义相关的属性
    private final String HOST = "127.0.0.1"; // 服务器的ip
    private final int PORT = 6667; // 服务器的端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    // 构造器, 完成初始化工作
    public EX_32_GroupChatClient() {
        try {
            // 得到选择器
            selector = Selector.open();
            // 连接服务器
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            // 设置非阻塞
            socketChannel.configureBlocking(false);
            // 将channel 注册到selector
            socketChannel.register(selector, SelectionKey.OP_READ);
            // 得到username
            username = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(username + " is ok...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 向服务器发送消息
    public void sendInfo(String info) {
        info = username + " 说: " + info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // 从服务器接收消息
    public void readInfo() {
        try {
            int readChannels = selector.select(2000);
            if (readChannels > 0) {
                // 有可以用的通道
                // 遍历得到selectionKey 集合
                // 1. 如果返回的>0, 表示已经获取到关注的事件
                // 2. selector.selectedKeys() 返回关注事件的集合
                // 通过selectionKeys 反向获取通道
                for (SelectionKey key : selector.selectedKeys()) {
                    // 根据key 对应的通道发生的事件做相应处理
                    if (key.isReadable()) {
                        // 通道发生read 事件, 即通道是可读的状态
                        // 得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 得到一个buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 读取
                        sc.read(buffer);
                        // 把读到的缓冲区的数据转成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());

                        selector.selectedKeys().remove(key);
                    }

                }
            } else {
                System.out.println("没有可以用的通道...");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 处理控制台输入的消息

    public static void main(String[] args) {
        // 启动我们客户端
        EX_32_GroupChatClient chatClient = new EX_32_GroupChatClient();
        // 启动一个线程, 每个3秒, 读取从服务器发送数据
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    chatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        // 发送数据给服务器端
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            chatClient.sendInfo(s);
        }
    }

}
