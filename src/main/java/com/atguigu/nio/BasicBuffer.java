package com.atguigu.nio;

import java.nio.IntBuffer;

public class BasicBuffer {
    public static void main(String[] args) {
        // 举例说明Buffer的使用(简单说明)
        // 创建一个Buffer,大小为5,即可以存放5个int
         IntBuffer intBuffer = IntBuffer.allocate(5);
        // // 向buffer存放数据
         for (int i = 0; i < intBuffer.capacity(); i++) {
             intBuffer.put(i * 2);
         }
        // // 如何从buffer读取数据
        // // 将buffer转换,读写切换
        /**
         * 1. 读写切换
         * public final Buffer flip() {
         *    limit = position;ƒ©
         *    position = 0;
         *    mark = -1;
         *    return this;
         *    }
         */
         intBuffer.flip();
         intBuffer.position(1);
         intBuffer.limit(3);
         while (intBuffer.hasRemaining()) {
             System.out.println(intBuffer.get());
         }
    }
}
