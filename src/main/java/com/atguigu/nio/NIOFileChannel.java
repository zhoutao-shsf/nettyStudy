package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 21:00
 **/
public class NIOFileChannel {

    public static void main(String[] args) throws IOException {
        String s = "hello, shangguig";

//        创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("./a.txt");

//        通过fileoutputStream获取channel
//        真实类型为FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();

//        创建一个buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

//        将string放入buffer中
        byteBuffer.put(s.getBytes());

        byteBuffer.flip();

//        将bufffer的数据写入到channel

        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
