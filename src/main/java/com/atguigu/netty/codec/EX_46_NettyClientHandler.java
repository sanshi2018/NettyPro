package com.atguigu.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class EX_46_NettyClientHandler extends ChannelInboundHandlerAdapter {
    // 当通道就绪就会触发该方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送Student对象到服务器
//        StudentPOJO.Student student = StudentPOJO.Student.newBuilder().setId(4).setName("张三").build();
//        ctx.writeAndFlush(student);
        // 随机发送strident或worker对象
        int random = (int) (Math.random() * 3);
//        MyDataInfo.MyMessage myMessage = null;
//        if (0 == random) {
//            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.StudentType)
//                    .setStudent(MyDataInfo.Student.newBuilder().setId(5).setName("张三").build()).build();
//        } else {
//            myMessage = MyDataInfo.MyMessage.newBuilder().setDataType(MyDataInfo.MyMessage.DataType.WorkerType)
//                    .setWorker(MyDataInfo.Worker.newBuilder().setAge(20).setName("李四").build()).build();
//        }
    }

    // 当通道有读取事件时, 就会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("服务器的回复是:" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("服务器的地址是:" + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
