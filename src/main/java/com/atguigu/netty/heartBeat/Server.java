package com.atguigu.netty.heartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class Server {
    public static void main(String[] args) {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))// 在bossGroup增加一个日志处理器
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 加入一个netty提供的IdleStateHandler
                            /*
                            说明
                            1. IdleStateHandler 是netty提供的处理空闲状态的处理器
                            2. long readerIdleTime : 表示多长时间没有读，就会发送一个心跳检测包检测是否连接
                            3. long writerIdleTime : 表示多长时间没有写，就会发送一个心跳检测包检测是否连接
                            4. long allIdleTime : 表示多长时间没有读写，就会发送一个心跳检测包检测是否连接
                            5. 文档说明
                                triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
                                read, write, or both operation for a while.
                            6. 当IdleStateEvent触发后，就会传递给管道的下一个handler去处理
                                通过调用(触发)下一个handler的userEventTiggered，在该方法中去处理IdleStateEvent(读空闲，写空闲，读写空闲)
                             */
                            ch.pipeline().addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            // 加入一个对空闲检测进一步处理的handler(自定义)
                            ch.pipeline().addLast(new ServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
