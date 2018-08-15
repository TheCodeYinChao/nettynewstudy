package cn.netty.privateprotocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author zyc
 * @date 2018/8/15 17:48
 * @Description:
 */
public class HeartBeatReqHandler extends ChannelHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message =(NettyMessage)(msg);
        if(message!=null){
            if(message.getHeader()!=null && message.getHeader().getType() == MessageType.LOGIN_REQ){
                ctx.executor().scheduleAtFixedRate(new HeartBeatReqHandler().HeartBeatTask(ctx),0,5000, TimeUnit.SECONDS);
            }else if(message.getHeader().getType() == MessageType.HEARTBEAT_REQ){
                System.out.println("client 接收到的服务心跳消息："+message);
            }
        }else{
            ctx.writeAndFlush(msg);
        }
    }

    private class  HeartBeatTask implements Runnable{
        ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heartBeat = buildHeartBeat();
            System.out.println("client heart beat to server："+ heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private NettyMessage buildHeartBeat() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ);
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(heartBeat!=null){
            heartBeat.cancel(true);
            heartBeat=null;

        }
        ctx.fireExceptionCaught(cause);
    }
}
