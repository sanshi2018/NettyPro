package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel02 {
    public static void main(String[] args) throws IOException {

        File file = new File("Tem/file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        fileChannel.read(byteBuffer);
        byteBuffer.flip();
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
