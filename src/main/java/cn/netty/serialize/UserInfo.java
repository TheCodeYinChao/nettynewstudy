package cn.netty.serialize;

import io.netty.buffer.ByteBuf;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * @author zyc
 * @date 2018/8/8 10:35
 * @Description: 序列化
 */
public class UserInfo implements Serializable {
    private String userName;
    private int userId;

    public UserInfo buildUsername(String userName){
        this.userName = userName;
        return this;
    }
    public  UserInfo buildUserId(int userId){
        this.userId = userId;
        return this;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] codeC(){
        ByteBuffer buf = ByteBuffer.allocate(1024);
        byte[] value = this.userName.getBytes();
        buf.putInt(value.length);
        buf.put(value);
        buf.putInt(this.userId);
        buf.flip();
        value = null;
        byte[] result = new byte[buf.remaining()];
        buf.get(result);
        return result;
    }

    public byte[] codeC(ByteBuffer buffer){
        buffer.clear();
        byte[] value = this.userName.getBytes();
        buffer.putInt(value.length);
        buffer.put(value);
        buffer.putInt(this.userId);
        buffer.flip();
        value = null;
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }
}
