package com.atguigu.netty.EX_89_protocolTcp.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.atguigu.netty.EX_89_protocolTcp.MessageProtocol;
import com.atguigu.netty.EX_89_protocolTcp.client.MyClientHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.StringUtil;

public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    private String last = null;
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {

        // 接收到数据并处理
        int len = messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();
        String str = new String(content, "utf-8");
//        System.out.println("服务器接收到的数据:");
//        System.out.println("长度=" + len);
//        System.out.println("内容=" + str);


        if (StringUtil.isNullOrEmpty(last)) {
            last = str;
        }
        // 如果当前str与last的值相差1, 则认为是连续的
        if (Integer.parseInt(str) - Integer.parseInt(last) != 1) {
            Logger.getLogger(MyServerHandler.class.getName()).log(Level.INFO, "发生乱序异常, 当前值=" + str + ", 上一个值=" + last);
        }

        last = str;

//        System.out.println("服务器接收到的数据包数量=" + (++this.count));

        // 回复消息
        String responseContent = "服务器回复的消息, len=" + len;
        int responseLen = responseContent.getBytes(CharsetUtil.UTF_8).length;
        byte[] responseContentBytes = responseContent.getBytes(CharsetUtil.UTF_8);

        // 构建一个协议包
        MessageProtocol messageProtocolResponse = new MessageProtocol();
        messageProtocolResponse.setLen(responseLen);
        messageProtocolResponse.setContent(responseContentBytes);

        channelHandlerContext.writeAndFlush(messageProtocolResponse);
    }
}
