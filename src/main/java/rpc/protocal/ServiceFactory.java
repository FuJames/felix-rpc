package rpc.protocal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import rpc.common.bean.RpcService;
import rpc.netty.server.NettyServer;
import rpc.zk.registry.RegistryManager;
import rpc.zk.registry.bean.ServiceInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuqianzhong
 * @date 18/6/5
 * 1. 获取所有基于注解的Rpc服务类,将服务bean缓存到本地
 * 2. 将服务注册到注册中心
 * 3. 启动netty服务
 */
public class ServiceFactory implements ApplicationContextAware,InitializingBean{
    private int port = 1300;
    private String ip;

    private static Map<String,Object> services = new ConcurrentHashMap<String,Object>();

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        //bean key & bean
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        System.out.println(serviceBeanMap);
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }
        if(serviceBeanMap != null && serviceBeanMap.size() > 0){
            if(isPortInUse(port)){
                port = getAvailablePort(port);
            }
            for(Map.Entry<String,Object> entry : serviceBeanMap.entrySet()){
                String serviceKey = entry.getValue().getClass().getAnnotation(RpcService.class).serviceKey();
                if(StringUtils.isNotBlank(serviceKey)){
                    services.put(serviceKey, entry.getValue());
                    try {
                        ServiceInfo serviceInfo = new ServiceInfo();
                        serviceInfo.setServiceKey(serviceKey);
                        serviceInfo.setIp(ip);
                        serviceInfo.setPort(port);
                        RegistryManager.getInstance().registerService(serviceInfo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            new NettyServer().doStart(port);
        }
    }

    private int getAvailablePort(int defaultPort) {
        int port = defaultPort;
        while (port < 65535) {
            if (!isPortInUse(port)) {
                return port;
            } else {
                port++;
            }
        }
        while (port > 0) {
            if (!isPortInUse(port)) {
                return port;
            } else {
                port--;
            }
        }
        throw new IllegalStateException("no available port");
    }

    private boolean isPortInUse(int port) {
        boolean inUse = false;
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            inUse = false;
        } catch (IOException e) {
            inUse = true;
        } finally {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                }
            }
        }
        return inUse;
    }

    public static Object getServiceBean(String serviceKey){
        return services.get(serviceKey);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
