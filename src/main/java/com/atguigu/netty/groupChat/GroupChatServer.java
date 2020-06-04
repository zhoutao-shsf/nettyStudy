package com.atguigu.netty.groupChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 08:49
 **/
public class GroupChatServer {
    private int port;

    public GroupChatServer(int port) {
        this.port = port;
    }

//    处理客户端请求
    public void run() throws InterruptedException {
        NioEventLoopGroup bossgGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossgGroup, workGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 128)
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
    //                                向pipline加入解码器
                                    pipeline.addLast("decoder", new StringDecoder());
    //                                向pipline加入编码器
                                    pipeline.addLast("encoder",new StringEncoder());
    //                                加入自己的handelr
                                    pipeline.addLast(new GroupChatServerHandler());
                                }
                            });

            System.out.println("server start");
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

//        监听关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossgGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChatServer(7000).run();
    }
}
