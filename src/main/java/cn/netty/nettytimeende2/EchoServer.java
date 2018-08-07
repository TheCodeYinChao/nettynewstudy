package cn.netty.nettytimeende2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author zyc
 * @date 2018/8/7 16:35
 * @Description:
 */
public class EchoServer {
    public void bind(int port){
        EventLoopGroup bossG = new NioEventLoopGroup();
        EventLoopGroup workerG = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossG,workerG)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ByteBuf bf = Unpooled.copiedBuffer("$_".getBytes());
                            //1024单条数据的最大长度
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,bf));
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new EchoHandlerServer());
                        }
                    });
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossG.shutdownGracefully();
            workerG.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoServer().bind(8081);
    }

}
