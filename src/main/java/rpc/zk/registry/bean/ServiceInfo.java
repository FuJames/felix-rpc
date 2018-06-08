package rpc.zk.registry.bean;

/**
 * @author fuqianzhong
 * @date 18/5/21
 */
public class ServiceInfo {
    private String serviceKey;

    private String ip;

    private int port;

    public ServiceInfo(){
    }

    public ServiceInfo(String serviceKey, String ip, int port) {
        this.serviceKey = serviceKey;
        this.ip = ip;
        this.port = port;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
