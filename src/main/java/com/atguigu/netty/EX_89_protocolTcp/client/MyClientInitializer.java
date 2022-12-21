package com.atguigu.netty.EX_89_protocolTcp.client;

import com.atguigu.netty.EX_89_protocolTcp.utilt.MyMessageDecoder;
import com.atguigu.netty.EX_89_protocolTcp.utilt.MyMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new MyMessageDecoder());
        ch.pipeline().addLast(new MyMessageEncoder());
        ch.pipeline().addLast(new MyClientHandler());
    }
}
