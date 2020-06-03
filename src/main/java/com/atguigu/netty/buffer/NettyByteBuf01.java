package com.atguigu.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-03 22:53
 **/
public class NettyByteBuf01 {
    public static void main(String[] args) {
//        创建一个bytebuf

//        创建对象，对象包括一个数组array
//        netty buf中不需要使用flip
//        底层维护了readindex 和 writeindex
        ByteBuf buffer = Unpooled.buffer(10);

        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }
    }

}
