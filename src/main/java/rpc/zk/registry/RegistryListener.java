package rpc.zk.registry;

import rpc.zk.registry.bean.ServerInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fuqianzhong
 * @date 18/6/5
 */
public class RegistryListener {
    private static List<ServiceChangeListener> serviceChangeListeners = new ArrayList<ServiceChangeListener>();
    public static void addListener(ServiceChangeListener serviceChangeListener) {
        serviceChangeListeners.add(serviceChangeListener);
    }

    public static void serverAdded(String serviceKey, ServerInfo serverInfo) throws Exception {
        List<ServiceChangeListener> listeners = new ArrayList<ServiceChangeListener>();
        listeners.addAll(serviceChangeListeners);
        for (ServiceChangeListener listener : listeners) {
            listener.serverAdded(serviceKey,serverInfo);
        }
    }
    public static void serverRemoved(String serviceKey, ServerInfo serverInfo) throws Exception {
        List<ServiceChangeListener> listeners = new ArrayList<ServiceChangeListener>();
        listeners.addAll(serviceChangeListeners);
        for (ServiceChangeListener listener : listeners) {
            listener.serverRemoved(serviceKey,serverInfo);
        }
    }

}
