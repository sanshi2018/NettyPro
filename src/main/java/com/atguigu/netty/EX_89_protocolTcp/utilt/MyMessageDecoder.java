package com.atguigu.netty.EX_89_protocolTcp.utilt;

import java.util.List;

import com.atguigu.netty.EX_89_protocolTcp.MessageProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;

public class MyMessageDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
//        System.out.println("MyMessageDecoder#decode 被调用");
        // 需要将得到的二进制字节码->MessageProtocol数据包(对象)
        int len = byteBuf.readInt();
        byte[] content = new byte[len];
        byteBuf.readBytes(content);

        // 封装成MessageProtocol对象, 放入到list, 传递给下一个handler业务处理
        MessageProtocol messageProtocol = new MessageProtocol();
        messageProtocol.setLen(len);
        messageProtocol.setContent(content);
        list.add(messageProtocol);
    }
}
