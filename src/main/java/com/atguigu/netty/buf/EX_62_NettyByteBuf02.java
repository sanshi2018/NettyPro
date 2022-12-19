package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class EX_62_NettyByteBuf02 {
    public static void main(String[] args) {
        // 创建一个byteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello,world!", CharsetUtil.UTF_8);
        // 使用相关的方法
        if (byteBuf.hasArray()) {
            byte[] content = byteBuf.array();
            // 将content转成字符串
            System.out.println(new String(content, CharsetUtil.UTF_8));
            System.out.println("byteBuf=" + byteBuf);
            // 返回ByteBuf的readerIndex和writerIndex之间的可读字节数
            System.out.println(byteBuf.readableBytes());
            // 返回ByteBuf的readerIndex
            System.out.println(byteBuf.readerIndex());
            // 返回ByteBuf的writerIndex
            System.out.println(byteBuf.writerIndex());
            // 返回指定索引处的字节
            System.out.println(byteBuf.getByte(0));
            // 返回指定索引处的字节
            int len = byteBuf.readableBytes();
            for (int i = 0; i < len; i++) {
                System.out.println((char) byteBuf.getByte(i));
            }
            // 指定索引处的字节
            System.out.println(byteBuf.getCharSequence(0, 4, CharsetUtil.UTF_8));
            System.out.println(byteBuf.getCharSequence(4, 6, CharsetUtil.UTF_8));
        }
    }
}
