package com.atguigu.netty.inboundhandlerAndOutBoundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 21:47
 **/
public class MyLongToByteEncoder extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Long msg, ByteBuf out) throws Exception {
        System.out.println("MyLongToByteEncoder encoder 被调用");

        System.out.println("msg=" + msg);

        out.writeLong(msg);
    }
}
