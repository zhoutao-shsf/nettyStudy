package com.atguigu.netty.groupChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: NettyPro
 * @description:
 * @author: zhoutao
 * @date: 2020-06-04 09:08
 **/
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {

//    定义一个channel组，管理所有的channel
//    GlobalEventExecutor.INSTANCE 全局的事件执行器，是一个单例
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-SS");

//    表示一旦当连接建立，第一个被执行
//    讲当前channel加入到 channleGroup中
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

//        该方法会自动将所有的channel遍历
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + "加入聊天\n");
//        管理和维护所有channel
//        将该客户加入聊天的信息推送给其他的在线客户端
        channelGroup.add(channel);
//

    }

//    表示channel处于活动的状态
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "上线了~");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        获取当前channel
        Channel channel = ctx.channel();
//        遍历channelGroup根据不同的情况回送不同的信息
        channelGroup.forEach(channel1 -> {
            if (channel1 != channel){
                channel1.writeAndFlush("[客户]" + channel.remoteAddress() + "发送消息" + msg + "/n");
            }else {
                channel1.writeAndFlush("[自己]发送了消息" + msg + "/n");
            }
        });
    }
// 表示channel出入非活动状态
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "离线了~");
    }

//    表示断开连接就会触发

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端+ 当期时间]"+ simpleDateFormat.format(new Date()) + channel.remoteAddress() + "离开了\n");
        System.out.println("channelGroup size" + channelGroup.size());

//        不需要执行该方法，会自动执行
//        channelGroup.remove(channel);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
