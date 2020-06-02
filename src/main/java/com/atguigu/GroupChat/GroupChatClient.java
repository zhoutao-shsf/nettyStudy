package com.atguigu.GroupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-28 08:36
 **/
public class GroupChatClient {

//    定义相关属性
    private final String HOST = "127.0.0.1";
    private final int PORT = 6669;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public GroupChatClient() throws IOException {

        selector = Selector.open();
//        连接服务器
//       socketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
//       设置非阻塞
        socketChannel.configureBlocking(false);
//        将channel组册到selector中
        socketChannel.register(selector, SelectionKey.OP_READ);
//        得到本地的地址
        username  = socketChannel.getLocalAddress().toString().substring(1);

    }

//    向服务器发送消息

    public void sendInfo(String info){
        info = username + "say:" + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    读取服务器消息
    public void readInfo(){
        try {
            int readChannels = selector.select();
            if (readChannels > 0){//说明有可读事件

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()){
//                        得到相关通道
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//                        读取
                        socketChannel.read(byteBuffer);

                        String s = new String(byteBuffer.array());

                        System.out.println(s.trim());
                        iterator.remove();
                    }
                }

            }else {
                System.out.println("没有可用通道");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
//        启动客户端
        GroupChatClient groupChatClient = new GroupChatClient();

//        启动线程，每隔三秒，读取数据
        new Thread(() -> {
            while (true) {
                groupChatClient.readInfo();
                try {
                    Thread.sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }

    }
}
