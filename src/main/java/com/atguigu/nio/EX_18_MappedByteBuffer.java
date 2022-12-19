package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 1. MappedByteBuffer 可让文件直接在内存(堆外内存)中修改,操作系统不需要拷贝一次
 *
 */
public class EX_18_MappedByteBuffer {
    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("Tem/file01.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1: FileChannel.MapMode.READ_WRITE 使用的读写模式
         * 参数2: 0 可以直接修改的起始位置
         * 参数3: 5 映射到内存的大小(不是索引位置),即将file01.txt的多少个字节映射到内存
         * 可以直接修改的范围就是0-5
         * 实际类型是DirectByteBuffer
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        map.put(0, (byte)'H');
        map.put(3, (byte)'9');

        randomAccessFile.close();

    }
}
