package rpc.route;

import org.springframework.util.CollectionUtils;
import rpc.netty.client.Client;
import rpc.zk.registry.ClientManager;

import java.util.List;
import java.util.Random;

/**
 * @author fuqianzhong
 * @date 18/6/8
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public Client selectClient(String serviceKey) {
        List<Client> clients = ClientManager.getInstance().getClients(serviceKey);
        if(!CollectionUtils.isEmpty(clients)){
            Random random = new Random();
            int randValue = random.nextInt(clients.size());
            return clients.get(randValue);
        }
        return null;
    }
}
