package rpc.zk.registry;

import org.springframework.util.CollectionUtils;
import rpc.common.config.ZkConfig;
import rpc.zk.curator.CuratorClient;
import rpc.zk.registry.bean.ServerInfo;
import rpc.zk.registry.bean.ServiceInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuqianzhong
 * @date 18/5/21
 * 服务注册中心,
 * 1. 服务端启动时,连接到注册中心,向zk写入节点,节点的父路径=/SERVER/{serviceKey}/ip:port,父节点类型为永久节点,子节点类型为临时节点
 * 2. 客户端启动时,连接到注册中心,根据serviceKey获取子节点列表,再获取ip:port列表,拿到服务端列表后,创建连接
 * 3. 客户端监听节点事件,当服务改变时,通知客户端
 */
public class RegistryManager {
    private static final String SERVICE_PATH = "/SERVER";
    private static final String PATH_SEPARATOR = "/";
    //保存serviceKey对应的服务器信息,一个serviceKey对应多台机器
    private final ConcurrentHashMap<String, Set<ServerInfo>> currentServers = new ConcurrentHashMap<String, Set<ServerInfo>>();

    private static CuratorClient curatorClient;

    private static final RegistryManager instance = new RegistryManager();
    private static volatile boolean isInit = false;

    public static RegistryManager getInstance() throws Exception {
        if (!isInit) {
            synchronized (RegistryManager.class) {
                if (!isInit) {
                    curatorClient = new CuratorClient(ZkConfig.ZK_HOSTS);
                    isInit = true;
                }
            }
        }
        return instance;
    }

    public void registerService(ServiceInfo serverConfig) throws Exception {
        String rootPath = SERVICE_PATH + PATH_SEPARATOR + serverConfig.getServiceKey();
        curatorClient.createIsAbsent(rootPath);//永久节点
        String path = rootPath + PATH_SEPARATOR + serverConfig.getIp() + ":" + serverConfig.getPort();
        if(!curatorClient.exists(path,true)){
            curatorClient.createEphemeral(path);
        }
        System.out.println("Register Service:" + serverConfig.getServiceKey() + ", ServerAddress = " + curatorClient.getChildren(rootPath));
    }

    public List<String> getServiceAddrList(String serviceKey) throws Exception {
        String path = SERVICE_PATH + PATH_SEPARATOR + serviceKey;
        List<String> children = curatorClient.getChildren(path);
        List<String> addrList = new ArrayList<String>();
        if(!CollectionUtils.isEmpty(children)){
            for(String child : children){
                addrList.add(child);
            }
        }
        return addrList;
    }

    public void notifyClients(String path) throws Exception {
        String serviceKey = ServerUtils.parseServiceKey(path);
        Set<ServerInfo> newServers = ServerUtils.parseServerAddressList(getServiceAddrList(serviceKey));
        Set<ServerInfo> oldServers = currentServers.get(serviceKey);
        Set<ServerInfo> toAddServers;
        Set<ServerInfo> toRemoveServers = Collections.emptySet();
        if(oldServers == null){
            toAddServers = newServers;
        }else {
            toRemoveServers = Collections.newSetFromMap(new ConcurrentHashMap<ServerInfo, Boolean>());
            toRemoveServers.addAll(oldServers);
            toRemoveServers.removeAll(newServers);
            toAddServers = Collections.newSetFromMap(new ConcurrentHashMap<ServerInfo, Boolean>());
            toAddServers.addAll(newServers);
            toAddServers.removeAll(oldServers);
        }
        if(!CollectionUtils.isEmpty(toAddServers)){
            for(ServerInfo serverInfo : toAddServers){
                RegistryListener.serverAdded(serviceKey,serverInfo);
            }
        }
        if(!CollectionUtils.isEmpty(toRemoveServers)){
            for(ServerInfo serverInfo : toAddServers){
                RegistryListener.serverRemoved(serviceKey,serverInfo);
            }
        }
    }

    public void addService(String serviceKey, ServerInfo serverInfo) {
        Set<ServerInfo> serverSet = currentServers.get(serviceKey);
        if(serverSet == null){
            serverSet = Collections.newSetFromMap(new ConcurrentHashMap<ServerInfo, Boolean>());
        }
        serverSet.add(serverInfo);
        currentServers.put(serviceKey, serverSet);
    }

    public void removeService(String serviceKey, ServerInfo serverInfo) {
        Set<ServerInfo> serverSet = currentServers.get(serviceKey);
        if (serverSet == null) {
            return;
        }
        currentServers.remove(serverInfo);
    }
}
