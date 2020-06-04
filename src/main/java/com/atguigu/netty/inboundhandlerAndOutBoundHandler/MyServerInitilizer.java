package com.atguigu.netty.inboundhandlerAndOutBoundHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 20:34
 **/
public class MyServerInitilizer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new MyByteToLongDecoder());

        pipeline.addLast(new MyLongToByteEncoder());
        pipeline.addLast(new MyServerHandler());
    }
}
