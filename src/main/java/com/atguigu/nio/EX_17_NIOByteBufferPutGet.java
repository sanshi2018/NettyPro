package com.atguigu.nio;

import java.nio.ByteBuffer;

public class EX_17_NIOByteBufferPutGet {
    public static void main(String[] args) {

        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);

        // 类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(9);
        byteBuffer.putChar('尚');
        byteBuffer.putShort((short)4);

        // 取出
        byteBuffer.flip();

        // 取出类型要对应，否则会出现数据错误

        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());

    }
}
