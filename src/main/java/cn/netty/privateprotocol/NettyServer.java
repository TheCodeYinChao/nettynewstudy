package cn.netty.privateprotocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;


/**
 * @author zyc
 * @date 2018/8/15 18:22
 * @Description:
 */
public class NettyServer {
    public void bind() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup)
                .option(ChannelOption.SO_BACKLOG,10)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyMessageDecoder(1024*1024,4,4));
                        ch.pipeline().addLast(new NettyMessageEncoder());
                        ch.pipeline().addLast("readTimeOutHandler",new ReadTimeoutHandler(50));
                        ch.pipeline().addLast("LoginAuthHandler",new LoginAuthRespHandler());
                        ch.pipeline().addLast("HeartBeatReqHandler",new HeartBeatRespHandler());
                    }
                });
        b.bind(NettyConstant.REMOTEIP,NettyConstant.PORT).sync();

        System.out.println("服务端启动成功");
    }

    public static void main(String[] args)throws Exception {
        new NettyServer().bind();
    }
}
