package com.atguigu.nio.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

public class EX_31_GroupChatServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    // 构造器
    // 初始化工作
    public EX_31_GroupChatServer() {
        try {
            // 得到选择器
            selector = Selector.open();
            // ServerSocketChannel
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞模式
            listenChannel.configureBlocking(false);
            // 将该listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 监听
    public void listen() {
        try {
            // 循环处理
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    // 有事件处理
                    // 遍历得到selectionKey 集合
                    // 1. 如果返回的>0, 表示已经获取到关注的事件
                    // 2. selector.selectedKeys() 返回关注事件的集合
                    // 通过selectionKeys 反向获取通道
                     selector.selectedKeys().forEach(selectionKey -> {
                         try {
                             // 根据key 对应的通道发生的事件做相应处理
                             if (selectionKey.isAcceptable()) { // 如果是OP_ACCEPT, 有新的客户端连接
                                 // 该客户端生成一个SocketChannel
                                 SocketChannel socketChannel = listenChannel.accept();
                                 // 将socketChannel 设置为非阻塞
                                 socketChannel.configureBlocking(false);
                                 // 将该socketChannel 注册到selector
                                 socketChannel.register(selector, SelectionKey.OP_READ);
                                 // 提示
                                 System.out.println(socketChannel.getRemoteAddress() + " 上线 ");
                             }
                             if (selectionKey.isReadable()) { // 发生OP_READ
                                 readData(selectionKey);
                             }
                         }catch (IOException e) {
                         }finally {
                             selector.selectedKeys().remove(selectionKey);
                         }


                     });
                } else {
                    System.out.println("等待2秒, 无连接");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 发生异常处理
        }
    }

    private void readData(SelectionKey selectionKey) {
        // 通过key 反向获取到对应channel
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) selectionKey.channel();
            // 得到一个Buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 将通道的数据读入到Buffer
            int len = channel.read(buffer);
            if (len > 0) {
                // 把缓冲区的数据转成字符串
                String msg = new String(buffer.array());
                // 输出该消息
                System.out.println("form 客户端: " + msg);
                // 向其他的客户端转发消息(去掉自己), 专门写一个方法来处理
                sendInfoToOtherClients(msg, channel);
            }
        }catch (IOException e) {
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了 ");
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                channel.close();
            }catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

    // 转发消息给其他客户(通道)
    private void sendInfoToOtherClients(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        // 遍历 所有注册到selector 上的 SocketChannel, 并排除 self
        for (SelectionKey key : selector.keys()) {
            // 通过key 取出对应的 SocketChannel
            Channel targetChannel = key.channel();
            // 排除自己
            if (targetChannel instanceof SocketChannel && targetChannel != self) {
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg 存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer 的数据写入到通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        EX_31_GroupChatServer groupChatServer = new EX_31_GroupChatServer();
        groupChatServer.listen();
    }
}
