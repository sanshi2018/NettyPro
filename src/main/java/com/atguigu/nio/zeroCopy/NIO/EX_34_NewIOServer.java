package com.atguigu.nio.zeroCopy.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class EX_34_NewIOServer {
    public static void main(String[] args) throws IOException {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7001);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(inetSocketAddress);

        Buffer buffer = ByteBuffer.allocate(4096);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            int readCount = 0;
            int total = 0;
            while (readCount != -1) {
                try {
                    readCount = socketChannel.read((ByteBuffer) buffer);
                    total += readCount;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                buffer.rewind();
            }
            System.out.println("文件读取完毕！兆字节数：" + total / 1024 / 1024);
        }
    }
}
