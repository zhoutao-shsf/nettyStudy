package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 22:02
 **/
// mapperByteBuffer 可以让文件在内存（堆外内存）修改， 操作系统不需要拷贝一次
public class MapperBytrBufferTest {

    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("a.txt", "rw");

        FileChannel channel = randomAccessFile.getChannel();

//        参数1
        channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
    }
}
