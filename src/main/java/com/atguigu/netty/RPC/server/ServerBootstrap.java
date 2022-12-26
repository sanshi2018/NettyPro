package com.atguigu.netty.RPC.server;

import com.atguigu.netty.RPC.server.netty.NettyServer;

// 启动一个服务提供者，就是NettyServer
public class ServerBootstrap {

    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws Exception {
        // 创建一个服务提供者
        NettyServer.startServer("127.0.0.1", 7000);
    }
}
