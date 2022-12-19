package com.atguigu.netty.codec;

import com.atguigu.netty.codec.proto.StudentPOJO;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

public class EX_44_NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup 和 WorkerGroup
        // 说明
        // 1. 创建两个线程组 bossGroup 和 workerGroup
        // 2. bossGroup 只是处理连接请求, 真正的和客户端业务处理,会交给 workerGroup 完成
        // 3. 两个都是无限循环
        // 4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
        // 默认实际 cpu 核数 * 2
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            // 创建服务器端的启动对象, 配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 1. 使用了链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
                    .channel(NioServerSocketChannel.class) // 使用 NioServerSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列得到连接个数 BACKLOG:积压的个数，代办事项
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 创建一个通道测试对象(匿名对象)
                        // 给 pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {

                            // 在 pipeline 中加入一个解码器
//                             ch.pipeline().addLast("decoder", new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
//                             ch.pipeline().addLast("decoder", new ProtobufDecoder(MyDataInfo.MyMessage.getDefaultInstance()));

                            System.out.println("客户 socketChannel hashcode=" + ch.hashCode());
                            // 可以使用一个集合管理 SocketChannel, 在推送消息时, 可以将业务加入到各个 channel 对应的
                            // NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
                            ch.pipeline().addLast(new EX_45_NettyServerHandler());
                        }
                    }); // 给我们的 workerGroup 的 EventLoop 对应的管道设置处理器
            System.out.println("...服务器 is ready...");
            // 绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
            // 启动服务器(并绑定端口)
            // bind 方法是异步的, sync 方法是同步阻塞的
            ChannelFuture cf = bootstrap.bind(6668).sync();
            // 给cf 注册监听器, 监控我们关心的事件
            cf.addListener(future -> {
                if (cf.isSuccess()) {
                    System.out.println("监听端口 6668 成功");
                } else {
                    System.out.println("监听端口 6668 失败");
                }
            });
            // 对关闭通道进行监听
            cf.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
