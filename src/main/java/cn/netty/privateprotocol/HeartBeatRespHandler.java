package cn.netty.privateprotocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author zyc
 * @date 2018/8/15 18:01
 * @Description:
 */
public class HeartBeatRespHandler extends ChannelHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message =(NettyMessage)(msg);
        if(message.getHeader()!=null && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.value()){
                System.out.println("server 接收到的服务心跳消息："+message);
                NettyMessage heartBeat = buildHeartBeat();
                System.out.println("server 发送 ： "+heartBeat);
                ctx.writeAndFlush(heartBeat);

        }else{
            ctx.writeAndFlush(msg);
        }
}

    private NettyMessage buildHeartBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_REQ.value());
        message.setHeader(header);
        return message;
    }
    }
