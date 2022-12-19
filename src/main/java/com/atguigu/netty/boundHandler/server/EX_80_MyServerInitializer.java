package com.atguigu.netty.boundHandler.server;

import com.atguigu.netty.boundHandler.utilt.MyByteTologDecoder;
import com.atguigu.netty.boundHandler.utilt.MyLongToByteEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class EX_80_MyServerInitializer extends ChannelInitializer<SocketChannel> {


    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new MyByteTologDecoder());
        ch.pipeline().addLast(new MyLongToByteEncoder());
        ch.pipeline().addLast(new MyServerHandler());
    }
}
