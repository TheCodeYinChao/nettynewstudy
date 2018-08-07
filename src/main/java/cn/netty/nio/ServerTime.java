package cn.netty.nio;

/**
 * @author zyc
 * @date 2018/8/3 11:33
 * @Description:
 */
public class ServerTime {
    public static void main(String[] args) {
        Server server = new Server(80);
        server.setStop(false);
        new Thread(server,"server").start();
    }
}
