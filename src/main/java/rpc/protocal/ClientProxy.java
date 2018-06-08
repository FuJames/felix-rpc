package rpc.protocal;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;
import rpc.common.bean.RpcRequest;
import rpc.common.bean.RpcResponse;
import rpc.netty.client.Client;
import rpc.route.RandomLoadBalance;
import rpc.zk.registry.ClientManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @author fuqianzhong
 * @date 18/6/4
 * 1. 根据serviceKey从注册中心获取服务端地址
 * 2. 根据服务端地址创建netty连接
 * 3. 为接口创建代理类(选择netty客户端,并且发送rpc请求)
 */
public class ClientProxy implements FactoryBean {

    private String serviceKey;

    private String interfaceName;

    private String serialize;

    private Object obj;

    public void init(){
        if (StringUtils.isEmpty(interfaceName)) {
            throw new IllegalArgumentException("invalid interface:" + interfaceName);
        }
        try {
            ClientManager.getInstance().registerClients(serviceKey);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Class<?> objClass = ClassUtils.getClass(classLoader, interfaceName);

            this.obj = Proxy.newProxyInstance(classLoader, new Class[]{objClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    RpcRequest request = new RpcRequest();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setCreateMillisTime(System.currentTimeMillis());
                    request.setClassName(method.getDeclaringClass().getName());
                    request.setMethodName(method.getName());
                    request.setParameterTypes(method.getParameterTypes());
                    request.setParameters(args);
                    request.setSerialize(serialize);
                    request.setServiceKey(serviceKey);

                    Client client = new RandomLoadBalance().selectClient(serviceKey);
                    System.out.println("[Client LB], client = " + client);

                    RpcResponse response = client.sendRequest(request);
                    if (response == null) {
                        return null;
                    }
                    if (response.isError()) {
                        throw response.getError();
                    } else {
                        return response.getResult();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object getObject() throws Exception {
        return this.obj;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getSerialize() {
        return serialize;
    }

    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }
}
