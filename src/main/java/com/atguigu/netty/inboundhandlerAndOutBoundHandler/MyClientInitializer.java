package com.atguigu.netty.inboundhandlerAndOutBoundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 21:44
 **/
public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

//        加入一个出栈的handler

        pipeline.addLast(new MyLongToByteEncoder());
        // 加入自定义的handler
        pipeline.addLast(new MyClientHandler());

        pipeline.addLast(new MyByteToLongDecoder());
    }
}
