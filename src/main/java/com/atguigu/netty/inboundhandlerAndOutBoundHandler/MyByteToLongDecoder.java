package com.atguigu.netty.inboundhandlerAndOutBoundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.List;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 21:06
 **/
public class MyByteToLongDecoder extends ByteToMessageDecoder {


    /**
     *
     * decoder 会根据接收的数据被调用多次，
     * list out 不为空，会将list 存到list
     *
     * @param ctx 上下文
     * @param in 入栈buffer
     * @param out list的集合，讲解码后的数据传给下一个handler
     * @throws Exception
     */

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) throws Exception {
//        long 8 个字节
        System.out.println("decoder 被调用");
        if (in.writableBytes() >= 8){
            out.add(in.readLong());
        }
    }



}
