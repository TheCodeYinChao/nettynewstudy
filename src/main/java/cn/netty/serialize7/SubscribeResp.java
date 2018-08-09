package cn.netty.serialize7;

import java.io.Serializable;

/**
 * @author zyc
 * @date 2018/8/8 17:34
 * @Description:
 */
public class SubscribeResp implements Serializable{

    private Integer subReqID;
    private String respCode;
    private String desc;

    public Integer getSubReqID() {
        return subReqID;
    }

    public void setSubReqID(Integer subReqID) {
        this.subReqID = subReqID;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "SubscribeResp{" +
                "subReqID=" + subReqID +
                ", respCode='" + respCode + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
