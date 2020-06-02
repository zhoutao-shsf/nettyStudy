package com.atguigu.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 21:47
 **/
public class NIOFileChannel4 {

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream("a.PNG");
        FileChannel channel1 = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("b.PNG");
        FileChannel channel2 = fileOutputStream.getChannel();
//
//        ByteBuffer byteBuffer = ByteBuffer.allocate()

        channel2.transferFrom(channel1, 0, channel1.size());

//         先关通道后关流
        channel1.close();
        channel2.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
