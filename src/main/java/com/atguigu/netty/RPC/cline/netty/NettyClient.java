package com.atguigu.netty.RPC.cline.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    // 创建线程池
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler client;

    // 初始化客户端
    private static void initClient() throws Exception {
        client = new NettyClientHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new NettyClientInitializer(client)); // 自定义一个初始化类
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 7000).sync();
            // 给关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }
    // 提供客户端服务
    // 编写方法使用代理模式，获取一个代理对象
    public Object getBean(final Class<?> serviceClass, final String providerName) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[]{serviceClass}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 客户端在调用服务器的api时，我们需要定义一个协议
                        // 比如我们要求 每次发消息时，都必须以某个字符串开头 "HelloService#hello#"
                        if (client == null) {
                            initClient();
                        }
                        // 设置要发给服务器端的信息
                        // providerName 协议头 args[0] 就是客户端调用api hello(???) 中的 ???
                        client.setPara(providerName + args[0]);
                        return executor.submit(client).get();
                    }
                });
    }

}
