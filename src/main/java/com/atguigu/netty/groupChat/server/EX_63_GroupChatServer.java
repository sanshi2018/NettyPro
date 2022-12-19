package com.atguigu.netty.groupChat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EX_63_GroupChatServer {
    private int port; // 监听端口
    public EX_63_GroupChatServer(int port) {
        this.port = port;
    }

    // 编写一个run方法，处理客户端请求
    public void run() throws Exception {
        // 创建两个线程组
        // 1. 主线程组，用于接收客户端的连接，但是不做任何处理，真正的和客户端业务处理，会交给workGroup完成
        // 2. 从线程组，主线程组会把任务转给从线程组，让从线程组去执行任务
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new EX_63_GroupChatServerInitializer());

            System.out.println("netty 服务器启动");
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            // 监听关闭事件
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new EX_63_GroupChatServer(8080).run();
    }

}
