package cn.netty.nettytimeende2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zyc
 * @date 2018/8/7 16:43
 * @Description:
 */
public class EchoHandlerServer extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("服务端接收到的消息："+body);
        body+="$_";
        ByteBuf buf = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(buf);
    }
}
