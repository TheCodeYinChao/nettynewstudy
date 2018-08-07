package cn.netty.nio;

/**
 * @author zyc
 * @date 2018/8/3 11:34
 * @Description:
 */
public class ClientTime {
    public static void main(String[] args) {
        Client client = new Client(80,"192.168.100.125");
        client.setStop(false);
        new Thread(client,"client").start();
    }
}
