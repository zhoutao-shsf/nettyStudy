package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-05-25 21:59
 **/
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(64);

        for (int i = 0; i < 10; i++) {
            buffer.put((byte) i);
        }

        buffer.flip();

        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();

        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }
    }
}
