package cn.netty.nettytime;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author zyc
 * @date 2018/8/7 15:27
 * @Description:
 */
public class TimeClientHandler extends ChannelHandlerAdapter {

    private final ByteBuf firstMessage;

    public TimeClientHandler() {
        byte[] req = "查询时间".getBytes();
        firstMessage= Unpooled.buffer(req.length);
        firstMessage.writeBytes(req);

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(firstMessage);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        System.out.println("接收到的服务端的数据：" + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
