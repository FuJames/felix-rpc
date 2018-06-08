package rpc.example.service;

import org.springframework.stereotype.Service;
import rpc.common.bean.RpcService;

/**
 * @author fuqianzhong
 * @date 18/6/7
 */
@RpcService(serviceKey = "DemoService_Key")
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello() {
        return "DemoServiceImpl.sayHello";
    }
}
