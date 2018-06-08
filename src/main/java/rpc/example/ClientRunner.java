package rpc.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rpc.example.service.DemoService;

/**
 * @author fuqianzhong
 * @date 18/6/8
 */
public class ClientRunner {
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/rpc-client.xml");
        DemoService demoService = (DemoService) ctx.getBean("demoServiceRpc");
        System.out.println("Result Is:" + demoService.sayHello());
    }
}
