package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-26 21:36
 **/
public class NIOClient {
    public static void main(String[] args) throws IOException {
//        等到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

//        设置非阻塞
        socketChannel.configureBlocking(false);

//        提供服务器的ip和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.0", 6666);

//        连接服务器

//        未连接
        if (!socketChannel.connect(inetSocketAddress)){
            while (!socketChannel.finishConnect()){
                System.out.println("do other things");
            }
        }

//        连接成功
        String s = "hello ,尚硅谷";

//         根据字节数组大小设定buffer
        ByteBuffer byteBuffer = ByteBuffer.wrap(s.getBytes());

//        将buffer数据写入channel
        socketChannel.write(byteBuffer);
    }
}
