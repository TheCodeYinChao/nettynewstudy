package cn.netty.buffer;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * @author zyc
 * @date 2018/8/13 11:30
 * @Description:
 */
public class BufDemo {
    public static void main(String[] args) {
        ByteBuffer b = ByteBuffer.allocate(88);
        String data = "权威指南";
        b.put(data.getBytes());
        b.flip();
        byte[] bytes = new byte[b.remaining()];
        b.get(bytes);
        String rs = new String(bytes);
        System.out.println(rs);
    }
}
