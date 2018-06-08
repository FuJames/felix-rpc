package rpc.zk.registry;

import rpc.zk.registry.bean.ServerInfo;

/**
 * @author fuqianzhong
 * @date 18/6/5
 */
public interface ServiceChangeListener {
    void serverAdded(String serviceKey, ServerInfo serverInfo) throws Exception;
    void serverRemoved(String serviceKey, ServerInfo serverInfo) throws Exception;
}
