package com.atguigu.netty.RPC.cline.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    private  NettyClientHandler client;
    NettyClientInitializer(NettyClientHandler client) {
        this.client = client;
    }
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new StringDecoder());
        ch.pipeline().addLast(new StringEncoder());
        ch.pipeline().addLast(client);
    }
}
