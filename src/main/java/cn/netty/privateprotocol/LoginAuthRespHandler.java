
package cn.netty.privateprotocol;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.awt.*;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zyc
 * @date 2018/8/15 17:32
 * @Description:
 */
public class LoginAuthRespHandler extends ChannelHandlerAdapter {
    private Map<String ,Boolean> nodeCheck = new ConcurrentHashMap<String,Boolean>();
    private String [] whiteList = {"127.0.0.1","192.168.100.125"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       NettyMessage message = (NettyMessage) msg;
       if(message.getHeader() != null && message.getHeader().getType()== MessageType.LOGIN_REQ.value()){
           String nodeIndex = ctx.channel().remoteAddress().toString();
           NettyMessage loginResp = null;
           if(nodeCheck.containsKey(nodeIndex)){
            loginResp = builResponse((byte)-1);
           }
           InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
           String ip = address.getAddress().getHostAddress();
           boolean isOk = false;
           for(String WIP:whiteList){
                if(WIP.equals(ip)){
                    isOk = true;
                    break;
                }
           }
           loginResp = isOk ? builResponse((byte)0):builResponse((byte)-1);
           if(isOk){
               nodeCheck.put(nodeIndex,true);
           }

           System.out.println("The login respons is :"+loginResp);
           ctx.writeAndFlush(loginResp);
       }else {
           ctx.writeAndFlush(msg);
       }
    }

    private NettyMessage builResponse(byte b) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.value());
        message.setHeader(header);
        message.setBody(b);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }


}
