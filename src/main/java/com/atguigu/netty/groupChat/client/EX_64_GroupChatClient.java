package com.atguigu.netty.groupChat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class EX_64_GroupChatClient {
    // 属性
    private final String host;
    private final int port;

    public EX_64_GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // 编写一个run方法，处理客户端的请求
    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        try{
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new EX_64_GroupChatClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            // 得到channel
            Channel channel = channelFuture.channel();
            System.out.println("-------" + channel.localAddress() + "Runing--------");
            // 客户端需要输入信息，创建一个扫描器
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNextLine()) {
                String msg = scanner.nextLine();
                // 通过channel发送到服务器端
                channel.writeAndFlush(msg + "\r \n");
            }
            // 监听关闭事件
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new EX_64_GroupChatClient("localhost", 8080).run();
    }
}
