package com.atguigu.netty.boundHandler.utilt;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteTologDecoder extends ByteToMessageDecoder {
    /**
     * decode 会根据接收的数据, 被调用多次, 直到确定没有新的元素被添加到list, 或者是ByteBuf 没有更多的可读字节为止
     * 如果list out 不为空, 就会将list的内容传递给下一个channelInboundHandler处理, 该处理器的方法也会被调用多次
     * @param ctx           the {@link ChannelHandlerContext} which this {@link ByteToMessageDecoder} belongs to
     * @param in            the {@link ByteBuf} from which to read data
     * @param out           the {@link List} to which decoded messages should be added
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteTologDecoder decode 被调用");
        // 将得到的二进制字节码转成一个Long类型的数据
        System.out.println(in.readableBytes());
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
