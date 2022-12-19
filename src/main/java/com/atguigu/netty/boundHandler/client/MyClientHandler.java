package com.atguigu.netty.boundHandler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("服务器的ip=" + ctx.channel().remoteAddress());
        System.out.println("收到服务器消息=" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyClientHandler 发送数据");
        // 发送的是一个long
        ctx.writeAndFlush(123456L);

        // 分析
        // 1. "abcdabcdabcdabcd", 是16个字节
        // 2. 该处理器的前一个handler是 MyLongToByteEncoder
        // 3. MyLongToByteEncoder 父类 MessageToByteEncoder
        // 4. MessageToByteEncoder 父类 MessageToMessageEncoder
        // 5. MessageToMessageEncoder#write() (该方法是我们自己不需要调用的)
        // 6. 在write()方法中会调用我们重写的 encode()方法
        // 7. 该方法会传入一个List集合
        // 8. 我们在encode()方法中, 添加了该Long
        // 9. 然后该数据就传递到了下一个handler的方法中, 该方法是我们自己需要调用的
        // ctx.writeAndFlush(Unpooled.copiedBuffer("abcdabcdabcdabcd", CharsetUtil.UTF_8));

    }
}
