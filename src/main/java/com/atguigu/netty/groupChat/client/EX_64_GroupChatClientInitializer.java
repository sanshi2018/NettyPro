package com.atguigu.netty.groupChat.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EX_64_GroupChatClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 向 pipeline 加入解码器
        pipeline.addLast("decoder", new StringDecoder());
        // 向 pipeline 加入编码器
        pipeline.addLast("encoder", new StringEncoder());
        // 加入自己的业务处理handler
        pipeline.addLast(new EX_64_GroupChatClientHandler());
    }

}
