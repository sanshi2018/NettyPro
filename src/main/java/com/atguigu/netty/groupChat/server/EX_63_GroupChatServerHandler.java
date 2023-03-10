package com.atguigu.netty.groupChat.server;

import com.atguigu.netty.groupChat.entity.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EX_63_GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    // 定义一个hashMap管理
//     private static Map<User, Channel> channels = new HashMap<>();

    // 定义一个集合，管理所有的channel
    // GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // handlerAdded 表示连接建立，一旦连接，第一个被执行
    // 将当前 channel 加入到 channelGroup
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户加入聊天的信息推送给其他在线的客户端
        // 该方法会将 channelGroup 中所有的 channel 遍历，并发送消息，我们不需要自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天 " + sdf.format(new Date()) + " \r \n");
        channelGroup.add(channel);
//        channels.put(new User(), channel);
    }

    // 表示 channel 处于活动状态，提示 xx 上线
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线了");
    }

    // 表示 channel 处于不活动状态，提示 xx 离线了
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线了");
    }
    // 断开连接，将 xx 客户离开信息推送给当前在线的客户
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 将该客户离开聊天的信息推送给其他在线的客户端
        // 该方法会将 channelGroup 中所有的 channel 遍历，并发送消息，我们不需要自己遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开了 " + sdf.format(new Date()) + " \r \n");
        System.out.println("channelGroup size = " + channelGroup.size());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 获取到当前的 channel
        Channel channel = ctx.channel();
        // 这时我们遍历 channelGroup，根据不同的情况，回送不同的消息
        channelGroup.forEach(ch -> {
            if (channel != ch) { // 不是当前的 channel，转发消息
                ch.writeAndFlush("[客户]" + channel.remoteAddress() + " 发送了消息 " + msg + " \r \n");
            } else { // 回显自己发送的消息给自己
                ch.writeAndFlush("[自己]发送了消息 " + msg + " \r \n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
