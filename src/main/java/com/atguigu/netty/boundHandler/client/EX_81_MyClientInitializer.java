package com.atguigu.netty.boundHandler.client;

import com.atguigu.netty.boundHandler.utilt.MyByteTologDecoder;
import com.atguigu.netty.boundHandler.utilt.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class EX_81_MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new MyLongToByteEncoder());
        ch.pipeline().addLast(new MyByteTologDecoder());
        ch.pipeline().addLast(new MyClientHandler());
    }
}
