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
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-02 19:45
 **/
/*
说明
1. 我们自定义一个 Handler 需要继续 netty 规定好的某个 HandlerAdapter(规范) 2. 这时我们自定义一个 Handler , 才能称为一个 handler
*/
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

//    读取客户端数据
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/*
1. ChannelHandlerContext ctx:上下文对象, 含有 管道 pipeline , 通道 channel, 地址 2. Object msg: 就是客户端发送的数据 默认 Object
*/
//        System.out.println("当前线程："+ Thread.currentThread().getName());
//        System.out.println("ctx=" + ctx);
//
//        System.out.println("=========================查看channel和pipeline的关系==========================");
//        Channel channel = ctx.channel();
//        ChannelPipeline pipeline = ctx.pipeline();
//        ByteBuf buf = (ByteBuf) msg;
//        System.out.println("客户端信息是："+ buf.toString(CharsetUtil.UTF_8));
//        System.out.println("远程地址是："+ channel.remoteAddress());


//        如果有一个非常耗时的任务->异步执行->提交该channel 对应的nioenentloop 的taskqueue中 防止阻塞需要有以下解决方案

//        解决方案1 用户自定义的普通方案
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("say hello", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


//        与上一个使用的是同一个线程，重新生成一个taskqueue
        ctx.channel().eventLoop().execute(() -> {
            try {
                Thread.sleep(20 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("say hi", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

//        解决方案2 用户定时任务->该任务提交到scheduleTaskQueue中
        ctx.channel().eventLoop().schedule((Runnable) () -> {
            try {
                Thread.sleep(10 * 1000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("say hello2", CharsetUtil.UTF_8));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },5 , TimeUnit.SECONDS);
    }

//    客户端读取数据完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据写入到缓存，并刷新
        //一般讲，我们对这个发送的数据进行编码
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端",CharsetUtil.UTF_8));


    }

//    处理异常，一般需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
