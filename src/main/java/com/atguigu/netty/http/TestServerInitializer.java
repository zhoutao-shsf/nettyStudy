package com.atguigu.netty.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-03 06:45
 **/
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
//        向管道加入处理器

//        得到管道
        ChannelPipeline pipeline = ch.pipeline();

//        加入netty的httpserverCodec codec =》 【coder - decoder】
//        HttpServerCodec 说明
//        netty 提供的http 编码解码器
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
//        增加自定义的handler

    }
}
