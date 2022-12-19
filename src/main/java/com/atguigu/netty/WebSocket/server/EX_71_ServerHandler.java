package com.atguigu.netty.WebSocket.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;

// 这里 TextWebSocketFrame 类型，表示一个文本帧(frame)
public class EX_71_ServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息: " + msg.text());
        // 回复消息
        ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器时间: " + LocalDateTime.now() + " " + msg.text()));
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    // 当 web 客户端连接后，触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // id 表示唯一的值，LongText 是唯一的 ShortText 不是唯一的
        System.out.println("handlerAdded 被调用: " + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用: " + ctx.channel().id().asShortText());
    }

    // 当 web 客户端关闭后，触发方法
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用: " + ctx.channel().id().asLongText());
        System.out.println("handlerRemoved 被调用: " + ctx.channel().id().asShortText());
    }
}
