package com.atguigu.nio;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 21:13
 **/
public class NIOFileChannel2 {

    public static void main(String[] args) throws IOException {
//        创建文件输入流

        File file = new File("./a.txt");

        FileInputStream fileInputStream = new FileInputStream(file);

//        通过输入流获取channel
        FileChannel channel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

//        将通道数据转入到buffer中
        channel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();

    }
}
