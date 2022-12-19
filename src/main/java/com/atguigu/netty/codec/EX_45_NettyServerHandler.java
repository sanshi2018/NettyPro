package com.atguigu.netty.codec;

import com.atguigu.netty.codec.proto.StudentPOJO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * 说明
 * 1. 我们自定义一个 Handler 需要继承 netty 规定好的某个 HandlerAdapter(规范)
 * 2. 这时我们自定义一个 Handler , 才能称为一个 handler
 */
//public class EX_45_NettyServerHandler extends SimpleChannelInboundHandler<MyDataInfo.MyMessage> {
public class EX_45_NettyServerHandler extends SimpleChannelInboundHandler<Object> {
//public class EX_45_NettyServerHandler extends ChannelInboundHandlerAdapter {
    // 读取数据实际(这里我们可以读取客户端发送的消息)
    /*
    当有数据可读时, 会触发该方法
    1. ChannelHandlerContext ctx: 上下文对象, 含有 管道 pipeline , 通道 channel, 地址
    2. Object msg: 就是客户端发送的数据 默认 Object
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 读取从客户端发送的 StudentPOJO.Student
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据 id=" + student.getId() + " 名字=" + student.getName());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        return;
    }

    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, MyDataInfo.MyMessage msg) throws Exception {
//        // 根据 dataType 来显示不同的信息
//        MyDataInfo.MyMessage.DataType dataType = msg.getDataType();
//        if (dataType == MyDataInfo.MyMessage.DataType.StudentType) {
//            MyDataInfo.Student student = msg.getStudent();
//            System.out.println("学生 id=" + student.getId() + " 名字=" + student.getName());
//        } else if (dataType == MyDataInfo.MyMessage.DataType.WorkerType) {
//            MyDataInfo.Worker worker = msg.getWorker();
//            System.out.println("工人 年龄=" + worker.getAge() + " 名字=" + worker.getName());
//        } else {
//            System.out.println("传输的类型不正确");
//        }
//
//    }


    // 数据读取完毕
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
