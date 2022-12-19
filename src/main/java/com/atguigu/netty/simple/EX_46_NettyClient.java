package com.atguigu.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EX_46_NettyClient {
    public static void main(String[] args) {
        // 客户端需要一个事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup();

        try {
            // 创建客户端启动对象
            // 注意客户端使用的不是 ServerBootstrap 而是 Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            // 设置相关参数
            bootstrap.group(eventExecutors) // 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类(反射)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EX_46_NettyClientHandler());
                        }
                    });
            System.out.println("客户端 ok ...");

            // 启动客户端链接服务器
            // 关于 ChannelFuture 要分析, 涉及到 netty 的异步模型
            // 当Future对象刚刚创建时，处于非完成状态，调用者可以通过返回的Channel Future来获取操作执行的状态，注册监听函数来执行完成后的操作
            ChannelFuture channelFuture =  bootstrap.connect("localhost",6668).sync();
            // IsDone 判断当操作是否完成
            boolean done = channelFuture.isDone();
            // IsSuccess 判断当操作是否成功
            // IsCancelled 判断当操作是否取消
            // IsCancellable 判断当操作是否可以取消
            // IsVoid 判断当操作是否为空
            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
