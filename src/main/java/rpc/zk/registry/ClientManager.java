package rpc.zk.registry;

import rpc.netty.client.Client;
import rpc.netty.client.NettyClient;
import rpc.zk.registry.bean.ServerInfo;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author fuqianzhong
 * @date 18/5/21
 * 客户端管理类,用于保存客户-服务端连接
 */
public class ClientManager {
    //保存server对应的连接,一个ip+port对应一个连接
    private final ConcurrentHashMap<ServerInfo, NettyClient> connections = new ConcurrentHashMap<ServerInfo, NettyClient>();
    //保存serviceKey对应的连接,一个serviceKey对应多个连接
    private final ConcurrentHashMap<String, List<Client>> serviceClients = new ConcurrentHashMap<String, List<Client>>();

    private ServiceChangeListener serviceChangeListener = new InnerServiceChangeListener();

    private static ClientManager instance = new ClientManager();

    private ClientManager(){
        RegistryListener.addListener(serviceChangeListener);
    }

    public static ClientManager getInstance(){return instance;}

    public void registerClients(String serviceKey) throws Exception {
        List<String> serviceAddrList = RegistryManager.getInstance().getServiceAddrList(serviceKey);
        Set<ServerInfo> serverSet = ServerUtils.parseServerAddressList(serviceAddrList);
        if(serverSet == null){
            throw new Exception("service unavailable for " + serviceKey);
        }
        for(ServerInfo serverInfo : serverSet){
            registerClient(serviceKey,serverInfo);
        }
    }

    public List<Client> getClients(String serviceKey){
        return serviceClients.get(serviceKey);
    }

    private void registerClient(String serviceKey,ServerInfo serverInfo) throws Exception {
        NettyClient client = connections.get(serverInfo);
        if(client == null){
            client = new NettyClient(serverInfo);
            connections.put(serverInfo,client);
        }
        List<Client> clients = serviceClients.get(serviceKey);
        if(clients == null){
            clients = new CopyOnWriteArrayList<Client>();
        }
        clients.add(client);
        serviceClients.put(serviceKey,clients);

        RegistryManager.getInstance().addService(serviceKey, serverInfo);

    }

   private void unregisterClient(String serviceKey,ServerInfo serverInfo) throws Exception {
        NettyClient client = connections.get(serverInfo);
        if(client != null && client.isValidate()){
            client.close();
            connections.remove(serverInfo);
            List<Client> clients = serviceClients.get(serviceKey);
            if(clients != null){
                clients.remove(client);
            }
        }

        RegistryManager.getInstance().removeService(serviceKey, serverInfo);

    }



    private class InnerServiceChangeListener implements ServiceChangeListener {

        @Override
        public void serverAdded(String serviceKey, ServerInfo serverInfo) throws Exception {
            registerClient(serviceKey,serverInfo);
        }

        @Override
        public void serverRemoved(String serviceKey, ServerInfo serverInfo) throws Exception {
            unregisterClient(serviceKey,serverInfo);
        }
    }
}
