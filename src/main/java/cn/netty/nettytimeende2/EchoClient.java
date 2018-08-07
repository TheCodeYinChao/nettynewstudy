package cn.netty.nettytimeende2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author zyc
 * @date 2018/8/7 16:35
 * @Description:
 */
public class EchoClient {
    public void connet(int port,String host){
        EventLoopGroup workerG = new NioEventLoopGroup();
        try {
           Bootstrap b = new Bootstrap();
            b.group(workerG)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            ByteBuf bf = Unpooled.copiedBuffer("$_".getBytes());
                            //1024单条数据的最大长度
                            sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,bf));
                            sc.pipeline().addLast(new StringDecoder());
                            sc.pipeline().addLast(new EchoHandlerClient());
                        }
                    });
            ChannelFuture f = b.connect(host,port).sync();

            f.channel().writeAndFlush(Unpooled.copiedBuffer("yaaaaa$_".getBytes()));
            f.channel().writeAndFlush(Unpooled.copiedBuffer("yaaaaa$_".getBytes()));
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerG.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new EchoClient().connet(8081,"127.0.0.1");
    }

}
