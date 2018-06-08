package rpc.common.bean;

import java.io.Serializable;

/**
 * @author fuqianzhong
 * @date 18/6/5
 */
public interface RpcSerializable extends Serializable{
    String getSerialize();

    void setSerialize(String serialize);

}
