package com.atguigu.netty.RPC.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    // 编写一个方法，完成对NettyServer的初始化和启动
    public static void startServer(String hostname, int port) {
        startServer0(hostname, port);
    }

    // 编写一个方法，完成对NettyServer的初始化和启动
    private static void startServer0(String hostname, int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new NettyServerInitializer()); // 给我们的workerGroup的EventLoop对应的管道设置处理器

            ChannelFuture sync = bootstrap.bind(hostname, port).sync();
            System.out.println("服务提供方开始提供服务...");
            sync.channel().closeFuture().sync();

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
