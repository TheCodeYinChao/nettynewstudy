package cn.netty.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * @author zyc
 * @date 2018/8/8 11:37
 * @Description:
 */
public class MainTest {
    public static void main(String[] args)throws Exception {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUsername("netty");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        System.out.println("jdk: "+ b.length);
        bos.close();
        System.out.println("-------------------");
        System.out.println("info:"+ userInfo.codeC().length);
    }
}
