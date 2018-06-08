package rpc.zk.registry;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rpc.zk.registry.bean.ServerInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fuqianzhong
 * @date 18/5/21
 */
public class ServerUtils {
    //IP:Port 列表
    public static Set<ServerInfo> parseServerAddressList(List<String> serverAddrList){
        if(CollectionUtils.isEmpty(serverAddrList)){
            return null;
        }
        Set<ServerInfo> serverSet = new HashSet<ServerInfo>();
        for(String addr : serverAddrList){
            String[] parts = addr.split(":");
            String host = parts[0];
            String port = parts[1];
            ServerInfo serverInfo = new ServerInfo();
            serverInfo.setIp(host);
            serverInfo.setPort(Integer.parseInt(port));
            serverSet.add(serverInfo);
        }
       return serverSet;
    }
    // /SERVER/{ServiceKey}/IP:PORT -> IP:PORT
    public static String parseServiceAddress(String path) {
        if(StringUtils.isEmpty(path)){
            return null;
        }
        int index = path.lastIndexOf("/");
        if(index > 0){
            return path.substring(index+1);
        }
        return null;
    }

    // /SERVER/ServiceKey/IP:PORT -> ServiceKey
    public static String parseServiceKey(String path) {
        if(StringUtils.isEmpty(path)){
            return null;
        }
        int lastIndex = path.lastIndexOf("/");

        if(lastIndex > 0){
            String temp = path.substring(0,lastIndex);
            int lastSecondIndex = temp.lastIndexOf("/");
            if(lastSecondIndex > 0){
                return temp.substring(lastSecondIndex + 1, lastIndex);
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(ServerUtils.parseServiceKey("/Server/ServiceKey/127.0.0.1:3388"));
        System.out.println(ServerUtils.parseServiceAddress("/Server/ServiceKey/127.0.0.1:3388"));
    }
}
