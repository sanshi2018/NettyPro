package com.atguigu.netty.EX_89_protocolTcp.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.atguigu.netty.EX_89_protocolTcp.MessageProtocol;
import com.atguigu.netty.EX_89_protocolTcp.server.MyServerHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {

    private int count;
    private String last = null;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 使用客户端发送10条数据
        for (int i = 0; i < 999999; i++) {
            String mes = i+"";
            byte[] content = mes.getBytes(CharsetUtil.UTF_8);
            int len = mes.getBytes(CharsetUtil.UTF_8).length;
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(len);
            messageProtocol.setContent(content);

            ctx.writeAndFlush(messageProtocol);
        }

        // 使用slf4j打印
        Logger.getLogger(MyClientHandler.class.getName()).log(Level.INFO, "消息发送完毕");



    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 使用slf4j打印异常
        Logger.getLogger(MyClientHandler.class.getName()).log(Level.SEVERE, "异常消息= " +cause.getMessage(), cause);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        int len = messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();
        String str = new String(content, "utf-8");
        System.out.println("服务器接收到的数据:");
        System.out.println("长度=" + len);
        System.out.println("内容=" + str);


        if (StringUtil.isNullOrEmpty(last)) {
            last = str;
        }
        // 如果当前str与last的值相差1, 则认为是连续的
        if (Integer.parseInt(str) - Integer.parseInt(last) != 1) {
            Logger.getLogger(MyServerHandler.class.getName()).log(Level.INFO, "发生乱序异常, 当前值=" + str + ", 上一个值=" + last);
        }

        last = str;

        System.out.println("服务器接收到的数据包数量=" + (++this.count));


    }
}
