package com.atguigu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class EX_53_TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // 得到管道
        ChannelPipeline pipeline = ch.pipeline();
        // 像管道添加处理器
        // 加入一个 netty 提供的 httpServerCodec codec => [coder - decoder]
        // HttpServerCodec 说明
        // 1. HttpServerCodec 是 netty 提供的处理 http 的编-解码器
        pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
        // 增加一个自定义的 handler
        pipeline.addLast("testHttpServerHandler", new EX_53_TestHttpServerHandler());
    }
}
