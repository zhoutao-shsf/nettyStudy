package com.atguigu.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-02 20:29
 **/
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //    客户端需要一个事件循环组
        NioEventLoopGroup group = new NioEventLoopGroup();

        try {
            //    创建客户端启动对象
            Bootstrap bootstrap = new Bootstrap();


//    设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class)  // 设置客户端通道的实现类（反射机制）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyClientHandler());  //加入自己的处理器
                        }
                    });
            System.out.println("client is ok");
//    启动客户端链接服务器端
//    关于channelFurture 涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6668).sync();
//        关闭通道监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }
}
