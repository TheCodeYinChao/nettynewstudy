package cn.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zyc
 * @date 2018/8/3 11:35
 * @Description: nio 服务端
 */
public class Server implements Runnable{
    private Selector selector;
    private ServerSocketChannel serverChannel;
    private volatile boolean stop;

    public Server(Integer port) {
        try {
            selector = Selector.open();
            serverChannel = ServerSocketChannel.open();
            serverChannel.configureBlocking(false);
            serverChannel.socket().bind(new InetSocketAddress(port));
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("server start port:"+port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (!stop){
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                SelectionKey selectionKey = null;
                while (iterator.hasNext()){
                    selectionKey = iterator.next();
                    iterator.remove();
                    handleInput(selectionKey);
                    if(selectionKey != null){
                        selectionKey.cancel();
                        if(selectionKey.channel() != null){
                            selectionKey.channel().close();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) {
        if(key.isValid()){
            if(key.isAcceptable()){
                try {
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    sc.register(selector,SelectionKey.OP_READ);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(key.isReadable()){
                try {
                    SocketChannel sc = (SocketChannel)key.channel();
                    ByteBuffer allocate = ByteBuffer.allocate(1024);
                    int read = sc.read(allocate);
                    if(read > 0){
                        allocate.flip();
                        byte[] bytes = new byte[allocate.remaining()];
                        allocate.get(bytes);
                        String body = new String(bytes,"UTF-8");
                        System.out.println("接收消息："+body);
                        doWrite(sc,"当前时间23:40");
                    }else if(read < 0){
                        key.cancel();
                        sc.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String s) {
        try {
            if(s != null && s.trim().length() > 0){
                byte[] bytes = s.getBytes();
                ByteBuffer allocate = ByteBuffer.allocate(bytes.length);
                allocate.put(bytes);
                allocate.flip();
                sc.write(allocate);
                System.out.println("发送的消息："+s);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
