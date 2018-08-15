package cn.netty.privateprotocol;

/**
 * @author zyc
 * @date 2018/8/15 15:50
 * @Description:
 */
public class NettyMessage {
    private Header header;//消息头
    private Object body;//返回值或者方法参数

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body='" + body + '\'' +
                '}';
    }
}
