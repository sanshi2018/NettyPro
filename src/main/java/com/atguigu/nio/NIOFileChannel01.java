package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        // 创建一个输入流->channel
        // 通过fileInputStream获取对应的FileChannel
        // 这个fileChannel真实类型是FileChannelImpl
        // FileChannel fileChannel = fileInputStream.getChannel();
        // 创建一个输出流->channel
        // 通过fileOutputStream获取对应的FileChannel
        // 这个fileChannel真实类型是FileChannelImpl
        // FileChannel fileChannel = fileOutputStream.getChannel();
        // 从输入流->channel读取数据
        // ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        // 从输出流->channel写入数据
        // ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        // 将byteBuffer数据写入到fileChannel->输出流
        // fileChannel.write(byteBuffer);
        // 将fileChannel->输入流的数据读取到byteBuffer
        // fileChannel.read(byteBuffer);

        // 检测项目是否存在out目录,如果不存在则创建
        String outPath = "Tem";
        java.io.File file = new java.io.File(outPath);
        if (!file.exists()) {
            file.mkdir();
        }

        // 1. 创建一个输出流->channel，输出到项目目录的out文件夹下的file01.txt,out不存在则新建
        FileOutputStream fileOutputStream = new FileOutputStream(outPath+"/file01.txt");

        // 获取对应的Channel
        // 2. 通过fileOutputStream获取对应的FileChannel
        // 这个fileChannel真实类型是FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        // 创建一个缓冲区 BetyBuffer
        // 3. 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 4. 将字符串放入byteBuffer
        byteBuffer.put("hello, world!".getBytes());
        // 5. 对byteBuffer进行flip
        byteBuffer.flip();
        // 6. 将byteBuffer数据写入到fileChannel->输出流
        fileChannel.write(byteBuffer);
        // 7. 关闭输出流
        fileOutputStream.close();



    }

}
