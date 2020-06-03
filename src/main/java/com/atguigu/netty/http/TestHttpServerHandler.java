package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-03 06:44
 **/

/*
1.还是继承ChannelInboundHandlerAdapter
2.HttpObject 表示客户端和服务器端相互通行的数据被封装成httpObject

 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

//    读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

//        判读msg是不是httprequest的类型
        if (msg instanceof HttpRequest){
            System.out.println("msg 类型= "+ msg.getClass());
            System.out.println("客户端地址 "+ ctx.channel().remoteAddress());

            HttpRequest httpRequest = (HttpRequest) msg;
            URI uri = new URI(httpRequest.uri());

            if ("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico ，不做响应");
                return;
            }
        }

//        回复消息给浏览器【http 协议】
        ByteBuf content = Unpooled.copiedBuffer("hello i am server", CharsetUtil.UTF_16);

//        构造http响应
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
        response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

//        返回response
        ctx.writeAndFlush(response);
    }
}
