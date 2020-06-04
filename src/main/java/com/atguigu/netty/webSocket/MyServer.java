package com.atguigu.netty.webSocket;

import com.atguigu.netty.heartbeat.MyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 11:34
 **/
public class MyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossgGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossgGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
//                            基于http协议，因此需要http 的编码解码器
                            pipeline.addLast(new HttpServerCodec());

//                            是以块的方式写，添加chunkerwritehandler处理器
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            说明
                            1. http 数据在传输过程中分段, 就是多个段聚合
                            2. 浏览器发送大量数据时，会发送多长http请求

                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /*
                            websocket 的数据以 帧的方式传输
                            webSocketFrame 下面有6个子类
                            浏览器发送请求时 ws://localhost:7000/xxx 对应的 url
                            WebSocketServerProtocolHandler核心功能将http协议升级为websocket协议，保持长联机
                            通过状态码 101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));

//                            自定义handler， 处理业务逻辑
                            pipeline.addLast(new  MyTextWebSocketFrameHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossgGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
