package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-26 19:57
 **/
public class NIOServer {

    public static void main(String[] args) throws IOException {
//        创建serversocketchannel -》serversocket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

//        创建selector
        Selector selector = Selector.open();

//        绑定端口，服务器监听
        serverSocketChannel.bind(new InetSocketAddress(6666));

//        设置为非阻塞
        serverSocketChannel.configureBlocking(false);
//        把serverSocket注册到selector 关心事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
//        循环等待客户端连接

        while (true){
//            等待1秒，没有响应就跳过
            if (selector.select(1000) == 0){
                System.out.println("wait 1m,no response");
                continue;
            }

//            如果selector.select>0说明有事件
//            获取selectkeys
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

//                如果是acceptable说明是新的客户端连接
                if (key.isAcceptable()){
//                    虽然accept方法为非阻塞，但是不会阻塞，因为已经有请求连接的
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);
//                    将socketchannel组册到selector中
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }

                if (key.isReadable()){
//                    通过key反向获取channel
                    SocketChannel channel =(SocketChannel) key.channel();
//                    通过key反向获取buffer
                    ByteBuffer byteBuffer = (ByteBuffer)key.attachment();

                    channel.read(byteBuffer);

                    System.out.println("from clinet" + new String(byteBuffer.array()));
                }

//                手动删除，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
