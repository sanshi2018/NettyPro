package com.atguigu.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EX_61_NettyByteBuf01 {
    public static void main(String[] args) {
        // 创建一个byteBuf
        // 1. 创建对象，该对象包含一个数组arr，是一个byte[10]
        // 2. 在netty的buffer中，不需要使用flip进行反转
        // 底层维护了 readerIndex 和 writerInde
        //
        //   废弃区域      可读区域       可写区域
        // 0 <= readerIndex <= writerIndex <= capacity
        ByteBuf byteBuf = Unpooled.buffer(10);
        for (int i = 0; i < 10; i++) {
            byteBuf.writeByte(i);
        }

        System.out.println("capacity=" + byteBuf.capacity()); // 10

        // 输出
        // 通过索引获取不会导致readerIndex和writerIndex的变化
//        for (int i = 0; i < byteBuf.capacity(); i++) {
//            System.out.println(byteBuf.getByte(i));
//        }

        // 使用readByte会导致readerIndex的增加
        for (int i = 0; i < byteBuf.capacity(); i++) {
            System.out.println(byteBuf.readByte());
        }

        // 打印readindex 和 writeIndex
        System.out.println("readerIndex=" + byteBuf.readerIndex() + " writerIndex=" + byteBuf.writerIndex());



    }
}
