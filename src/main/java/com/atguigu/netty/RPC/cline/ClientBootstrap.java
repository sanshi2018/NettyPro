package com.atguigu.netty.RPC.cline;

import com.atguigu.netty.RPC.cline.netty.NettyClient;
import com.atguigu.netty.RPC.common.HelloService;

public class ClientBootstrap {

    // 定义协议头
    public static final String providerName = "HelloService#hello#";

    public static void main(String[] args) throws Exception {
        // 创建一个消费者
        NettyClient customer = new NettyClient();


        // 创建一个代理对象
        HelloService service = (HelloService) customer.getBean(HelloService.class, providerName);

        for (; ; ) {
//            Thread.sleep(2 * 1000);

            // 通过代理对象调用服务提供者的方法（服务）
            String res = service.hello("你好 dubbo~");
            System.out.println("res= " + res);
        }
    }
}
