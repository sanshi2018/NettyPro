package com.atguigu.nio.zeroCopy.NIO;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class EX_34_NewIOClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 7001));
        String fileName = "Tem/Telegram.dmg";

        // 在这里将 fileName 发送给服务器
        // ...
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long startTime   = System.currentTimeMillis();
        // 在 Linux 下一个 transferTo 方法就可以完成传输
        // 在 Windows 下一次调用 transferTo 只能发送 8M，就需要分段传输文件，而且要主要传输时的位置
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送总字节数：" + transferCount + ", 耗时：" + (System.currentTimeMillis() - startTime));
        fileChannel.close();
    }
}
