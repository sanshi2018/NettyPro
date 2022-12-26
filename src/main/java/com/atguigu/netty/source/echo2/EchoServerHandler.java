/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.atguigu.netty.source.echo2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    // group 就是充当业务线程池，可以将任务提交到 group 中，由 group 中的线程去执行
    // 这里创建了一个线程池，线程池中有 16 个线程
    static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("EchoServerHandler的线程=" + Thread.currentThread().getName());
        // 比如这里有一个非常耗时的业务-> 异步执行 -> 提交该 channel 对应的 NIOEventLoop 的 taskQueue 中
        // 解决方案1 用户程序自定义的普通任务
        /**
         * 这个任务是在一个线程中执行的, 但是这个线程是在 NIOEventLoop 的线程中执行的
         * 也就是说, 这个任务是在 NIOEventLoop 的线程中执行的, 使用Netty处理workerGroup的线程，进而导致netty变慢
         */
//        ctx.channel().eventLoop().execute(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(5 * 1000);
//                    // 输出线程名称
//                    System.out.println("EchoServerHandler execute 线程是= : " + Thread.currentThread().getName());
//                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~2", CharsetUtil.UTF_8));
//
//                } catch (InterruptedException e) {
//                    System.out.println("发生异常" + e.getMessage());
//                }
//            }
//        });

        // 将任务提交到 group 中，由 group 中的线程去执行
        /**
         * 将任务提交给handler的线程池，这样就不会阻塞NIOEventLoop的线程
         * 任务执行完毕后，将结果返回给NIOEventLoop的线程
         * 具体查看ctx的write的方法，会将结果写入到NIOEventLoop的线程中
         */
        group.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                // 接受客户端消息
                ByteBuf buf = (ByteBuf) msg;
                System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
                Thread.sleep(5 * 1000);
                // 输出线程名称
                System.out.println("group submit 线程是= : " + Thread.currentThread().getName());
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~jay", CharsetUtil.UTF_8));
                return null;
            }
        });

        System.out.println("go on ...");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
