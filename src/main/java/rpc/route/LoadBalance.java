package rpc.route;

import rpc.netty.client.Client;

/**
 * @author fuqianzhong
 * @date 18/6/8
 */
public interface LoadBalance {
    Client selectClient(String serviceKey);
}
