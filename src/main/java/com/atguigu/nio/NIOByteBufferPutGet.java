package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 21:56
 **/
public class NIOByteBufferPutGet {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        buffer.putInt(100);
        buffer.putChar('d');
        buffer.putLong(23);

        buffer.flip();

        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());
//
//        System.out.println(buffer.getChar());
//        System.out.println(buffer.getInt());
//
//      System.out.println(buffer.getLong());
    }
}
