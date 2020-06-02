package com.atguigu.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-27 19:49
 **/
public class GroupChatServer {

//    定义属性
    private Selector selector;
    private ServerSocketChannel listenchannel;
    private static final int PORT = 6669;

//    初始化工作
    public GroupChatServer() {
        try {
//            得到选择器
            selector = Selector.open();

            listenchannel = ServerSocketChannel.open();

            listenchannel.socket().bind(new InetSocketAddress(PORT));

            listenchannel.configureBlocking(false);

            listenchannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    监听
    public void listen(){
        try {
//            循环处理
            while (true){
                int count = selector.select();
                if (count > 0){// 有事件处理
//                    遍历得到selector集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
//                        监听的是连接事件
                        if (selectionKey.isAcceptable()){
                            SocketChannel socketChannel = listenchannel.accept();

                            socketChannel.configureBlocking(false);
//                            注册socketChannel组册到selector中
                            socketChannel.register(selector, SelectionKey.OP_WRITE);
//                            提示
                            System.out.println(socketChannel.getRemoteAddress() + "上线");
                        }

//                        如果通道发生read事件，即通道是可读状态
                        if (selectionKey.isReadable()){
                            readDate(selectionKey);
                        }
//                        手动删除key防止重复操作
                        iterator.remove();
                    }

                }else {
                    System.out.println("waiting...");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    读取客户端消息
    private void readDate(SelectionKey key){
//        定义一个socketchannel
        SocketChannel channel = null;
        try {
//            去到关联的channel
            channel = (SocketChannel) key.channel();

//            创建缓冲buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);

//            根据count的值处理
            if (count > 0){
//                把缓冲区的数据转成字符串
                String s = new String(buffer.array());

                System.out.println("from client" + s);

//                向其他客户端转发消息(去掉自己)
                sendInfoToOtherClinet(s, channel);

            }

        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress() + "off");
//                捕获
                key.cancel();

                channel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

//    转发消息给其他客户
    private void sendInfoToOtherClinet(String msg, SocketChannel self) throws IOException {

        System.out.println("server send information...");
//        遍历所有到selector中key并排查自己
        for (SelectionKey key : selector.selectedKeys()){

//            通过key取出对应的socketchannel
            Channel targetchannel = key.channel();

//            排除自己
            if (targetchannel instanceof SocketChannel && targetchannel != self){
//                转型
                SocketChannel dest = (SocketChannel) targetchannel;
//                将msg存储到buffer中
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());

//                将buffer的数据写入到通道
                dest.write(buffer);
            }

        }
    }

    public static void main(String[] args) {
//        c创建一个服务器对象
        GroupChatServer chatServer = new GroupChatServer();

        chatServer.listen();
    }
}
