package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 22:22
 **/

// scattering 将数据写入buffer时，采用buffer数组，依次写入
//  gathering 将数据从buffer读取时，采用buffer数组，依次读取
public class ScatteringAndGatheringTest {

    public static void main(String[] args) throws Exception {
//        使用ServerSocketChannel 和 socketchannel

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

//        绑定端口到socket上，并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

//        创建bytebuffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

//         等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;


        while (true){
            int byteRead = 0;

            while (byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println(byteRead);

                Arrays.stream(byteBuffers).map(byteBuffer -> "position =" + byteBuffer.position() + "limit=" +
                byteBuffer.limit()).forEach(System.out :: println);
            }
            Arrays.stream(byteBuffers).forEach(byteBuffer -> byteBuffer.flip());


            long byteWrite = 0;
            while (byteWrite < messageLength){
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;

            }

            Arrays.stream(byteBuffers).forEach(byteBuffer -> byteBuffer.clear());

            System.out.println("byteRead:=" + byteRead + " byteWrite=" + byteWrite + ", messagelength" + messageLength);
        }


    }
}
