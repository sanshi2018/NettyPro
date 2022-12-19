package com.atguigu.nio;

import java.nio.ByteBuffer;

public class EX_17_ReadOnlyBuffer {
    public static void main(String[] args) {
        // 创建一个Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte)i);
        }
        byteBuffer.flip();

        // 得到一个只读的Buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        // 读取
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }

         readOnlyBuffer.put((byte)100); // java.nio.ReadOnlyBufferException
    }
}
