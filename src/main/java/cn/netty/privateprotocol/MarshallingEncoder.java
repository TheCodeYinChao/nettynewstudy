package cn.netty.privateprotocol;


import cn.netty.serialize9.MarshallingCodeCFactory;
import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.Marshaller;

/**
 * @author zyc
 * @date 2018/8/15 16:23
 * @Description:
 */
public class MarshallingEncoder {
    private static final  byte[] LENGTH_PLACEHOLDER = new byte[4];
    Marshaller marshaller;

    public MarshallingEncoder(){
       try {
           this.marshaller = MarshallingCodeCFactory.bulidMarshalling();
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    public void encode(Object msg, ByteBuf out)throws Exception{
        try{
            int lengthPos = out.writerIndex();
            out.writeBytes(LENGTH_PLACEHOLDER);
            ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
            marshaller.start(output);
            marshaller.writeObject(msg);
            marshaller.finish();
            out.setInt(lengthPos,out.writerIndex() - lengthPos - 4);
        }finally {
            marshaller.close();
        }


    }
}
