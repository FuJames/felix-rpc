package rpc.zk.registry.bean;

/**
 * @author fuqianzhong
 * @date 18/5/21
 * 服务端信息
 */
public class ServerInfo {
    private String ip;

    private int port;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ServerInfo) {
            ServerInfo serverInfo = (ServerInfo) obj;
            return this.ip.equals(serverInfo.ip) && this.port == serverInfo.port;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return ip.hashCode() + port;
    }

    @Override
    public String toString() {
        return "Server Info, ip = " + ip + ", port = " + port;
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
