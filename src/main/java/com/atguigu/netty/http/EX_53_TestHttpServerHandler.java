package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * 说明
 * 1. SimpleChannelInboundHandler 是 ChannelInboundHandlerAdapter
 * 2. HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject
 */
public class EX_53_TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    // 读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // 判断 msg 是不是 httprequest 请求
        if (msg instanceof HttpObject) {
            System.out.println("msg 类型 = " + msg.getClass());
            System.out.println("客户端地址 = " + ctx.channel().remoteAddress());

            // 获取到
             HttpRequest httpRequest = (HttpRequest) msg;
            // 获取 uri, 过滤指定的资源
//            String uri = httpRequest.uri();
            URI uri = new URI(httpRequest.uri());
            if ("/favicon.ico".equals(uri.getPath())) {
                System.out.println("请求了 favicon.ico, 不做响应");
                return;
            }
            // 回信息给浏览器 [http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello, 我是服务器", CharsetUtil.UTF_8);

            // 创建一个http响应, 即httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            // 设置响应头 utf-8
            response.headers().set("Content-Type", "text/plain; charset=UTF-8");
            // 设置响应长度
            response.headers().set("Content-Length", content.readableBytes());

            // 将构建好 response 返回
            ctx.writeAndFlush(response);
        }
    }
}
