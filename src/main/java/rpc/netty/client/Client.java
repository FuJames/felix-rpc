package rpc.netty.client;


import rpc.common.bean.RpcRequest;
import rpc.common.bean.RpcResponse;

/**
 * @author fuqianzhong
 * @date 18/6/5
 */
public interface Client {
    RpcResponse sendRequest(RpcRequest request);
}
