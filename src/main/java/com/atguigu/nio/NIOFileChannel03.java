package com.atguigu.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        File  file = new File("Tem/file01.txt");

        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("Tem/file02.txt");
        FileChannel fileChannel1 = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        int i=0;
        while (true) {
            byteBuffer.clear();
            int read = fileChannel.read(byteBuffer);
            if (read == -1) {
                break;
            }
            i++;
            byteBuffer.flip();
            fileChannel1.write(byteBuffer);
        }
        System.out.println(i);

//        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//
//        fileChannel.read(byteBuffer);
//        byteBuffer.put("\nappendContent".getBytes());
//
//        byteBuffer.flip();
//
//        fileChannel1.write(byteBuffer);

        fileInputStream.close();
        fileOutputStream.close();



    }
}
