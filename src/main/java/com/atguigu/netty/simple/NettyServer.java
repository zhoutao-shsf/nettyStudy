package com.atguigu.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-02 07:29
 **/
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
//       创建boosgroup 和 WorkerGroup
//        创建两个线程组 bossgroup 和 workgroup
//        bossgroup 只是处理连接请求 ，真正和客户端的业务处理交给workergroup完成
//        两个都是无限循环
//        bossfroup 和 workergroup 含有的子线程（nioenentloop）是 cpu核数量乘以2
            NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
            NioEventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
    //        创建服务器端的启动对象，配置启动参数
                ServerBootstrap bootstrap = new ServerBootstrap();

    //        使用链式编程设置
                bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                        .channel(NioServerSocketChannel.class) //使用niosocketChannel作为通道实现
                        .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到的连接个数
                        .handler(null)    //该handler对应的是bossgroup ，childhandler对应的是workerhandler
                        .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                        .childHandler(new ChannelInitializer<SocketChannel>() { //创建一个通道测试对象（匿名对象）

                            //                        向通道
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                socketChannel.pipeline().addLast(new NettyServerHandler());
                            }
                        }); //给workerGroup 的EventLoop 对应的管道设置处理器

                System.out.println("。。。。服务器 is readdy...");

    //        绑定端口生成channelFurture
    //        启动服务器并绑定端口
                ChannelFuture channelFuture = bootstrap.bind(6668).sync();

//             给channelFurture 注册监听器，监听我们关心的事件
                channelFuture.addListener((ChannelFutureListener) future -> {
                    if (future.isSuccess()){
                        System.out.println("监听端口成功");
                    }else {
                        System.out.println("监听端口失败");
                    }
                });
    //        对关闭通道进行监听
                channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
