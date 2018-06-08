package rpc.common.bean;


import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuqianzhong
 * @date 18/6/5
 */
public class RpcFuturePool {
    private static final ConcurrentHashMap<String, RpcFuture> futureMap = new ConcurrentHashMap<String, RpcFuture>();

    public static void addFuture(String requestId, RpcFuture future) {
        futureMap.put(requestId, future);
    }

    public static void removeFuture(String requestId) {
        futureMap.remove(requestId);
    }


    public static void reciveResponse(RpcResponse response) {
        RpcFuture future = futureMap.get(response.getRequestId());
        future.setResponse(response);
    }
}
