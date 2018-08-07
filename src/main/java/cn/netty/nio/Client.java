package cn.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zyc
 * @date 2018/8/3 14:14
 * @Description: nio 客户端
 */
public class Client implements Runnable{
    private int port;
    private String ip;
    private volatile boolean stop;
    private Selector selector;
    private SocketChannel socketChannel;

    public Client(int port, String ip) {
        this.port = port;
        this.ip = ip;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            if(socketChannel.connect(new InetSocketAddress(ip,port))){
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite(socketChannel);
            }else{
                socketChannel.register(selector, SelectionKey.OP_CONNECT);
            }
        } catch (Exception e) {
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
                SelectionKey key = null;
                while (iterator.hasNext()){
                    try {
                        key = iterator.next();
                        iterator.remove();
                        handlePut(key);
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
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

    private void handlePut(SelectionKey key) throws Exception {
       if(key.isValid()){
           SocketChannel sc = (SocketChannel) key.channel();
           if(key.isConnectable()) {
               if (sc.finishConnect()) {
                   sc.register(selector, SelectionKey.OP_READ);
                   doWrite(sc);
               } else {
                   System.exit(1);
               }
           }
           if(key.isReadable()){
               ByteBuffer allocate = ByteBuffer.allocate(1024);
               int read = sc.read(allocate);
               if(read > 0){
                   allocate.flip();
                   byte[] bytes = new byte[allocate.remaining()];
                   allocate.get(bytes);
                   String s = new String(bytes, "UTF-8");
                   System.out.println("接收的消息 Now is :" + s);
                   this.stop = true;
               }else if(read < 0){
                   key.channel();
                   sc.close();
               }
           }
       }
    }

    private void doWrite(SocketChannel sc) throws Exception{
        byte[] bytes = "查询时间".getBytes();
        ByteBuffer allocate = ByteBuffer.allocate(bytes.length);
        allocate.put(bytes);
        allocate.flip();
        System.out.println("发送的消息：" + new String(bytes));
        sc.write(allocate);
        if(!allocate.hasRemaining()){
            System.out.println("消息全部发送！");
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
