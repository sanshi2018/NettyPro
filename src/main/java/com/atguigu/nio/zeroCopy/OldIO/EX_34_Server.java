package com.atguigu.nio.zeroCopy.OldIO;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class EX_34_Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);

        while (true) {
            Socket socket = serverSocket.accept();

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            byte[] buffer = new byte[4096];

            while (true) {
                int readCount = dataInputStream.read(buffer, 0, buffer.length);
                if (readCount == -1) {
                    break;
                }
            }
            System.out.println("文件读取完毕！");
        }
    }
}
