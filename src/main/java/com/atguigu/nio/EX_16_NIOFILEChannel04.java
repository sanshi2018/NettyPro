package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class EX_16_NIOFILEChannel04 {
    public static void main(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("Tem/file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("Tem/file02.txt");

        FileChannel sourceChannel = fileInputStream.getChannel();
        FileChannel destChannel = fileOutputStream.getChannel();

        destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());

        sourceChannel.close();
        destChannel.close();
        fileInputStream.close();
        fileOutputStream.close();

    }
}
