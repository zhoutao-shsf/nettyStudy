package com.atguigu.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 11:05
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
//                            加入netty 提供的 idlestatehandler
                            /*
                            netty 提供的 状态空闲处理器
                            参数含有：1 表示多长时间未读，就会发送一个心跳检测包检测是否连接
                            2. 多长时间未写
                            3.多长时间没有读写

                            Triggers an {@link IdleStateEvent} when a {@link Channel} has not performed
 * read, write, or both operation for a while.
                            IdleStateEvent 触发后会传递给管道的下一个handler 处理
                            通过触发下一个handler 的userEventTiggered，在该方法中处理IdleStateEvent（读空闲，写控血）
                             */
                            pipeline.addLast(new IdleStateHandler(3,5,4, TimeUnit.SECONDS));
//                            加入对空闲检测的处理
                            pipeline.addLast(new MyServerHandler());

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
