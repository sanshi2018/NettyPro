package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 说明
 * 1. 我们自定义一个 Handler 需要继承 netty 规定好的某个 HandlerAdapter(规范)
 * 2. 这时我们自定义一个 Handler , 才能称为一个 handler
 */
public class EX_45_NettyServerHandler extends ChannelInboundHandlerAdapter {
    // 读取数据实际(这里我们可以读取客户端发送的消息)
    /*
    当有数据可读时, 会触发该方法
    1. ChannelHandlerContext ctx: 上下文对象, 含有 管道 pipeline , 通道 channel, 地址
    2. Object msg: 就是客户端发送的数据 默认 Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 比如这里有一个非常耗时的业务-> 异步执行 -> 提交该 channel 对应的 NIOEventLoop 的 taskQueue 中
        // 解决方案1 用户程序自定义的普通任务
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~2", CharsetUtil.UTF_8));

                } catch (InterruptedException e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        });

        // 解决方案2 用户自定义定时任务 -》 该任务是提交到 scheduleTaskQueue 中
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5 * 1000);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~3", CharsetUtil.UTF_8));

                } catch (InterruptedException e) {
                    System.out.println("发生异常" + e.getMessage());
                }
            }
        }, 5, TimeUnit.SECONDS);




//        System.out.println("服务器读取线程 " + Thread.currentThread().getName());
//        System.out.println("server ctx = " + ctx);
//        System.out.println("看看 channel 和 pipeline 的关系");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链接, 出站入站
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("客户端发送消息是:" + byteBuf.toString(CharsetUtil.UTF_8));
//        System.out.println("客户端地址:" + ctx.channel().remoteAddress());
    }

    // 数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // writeAndFlush 是 write + flush
        // 将数据写入到缓存, 并刷新
        // 一般讲, 我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer(ctx.channel().localAddress()+" : "+ctx.channel().remoteAddress()+"通道读取完毕\n", CharsetUtil.UTF_8));
    }

    // 处理异常, 一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
