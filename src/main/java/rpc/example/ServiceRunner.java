package rpc.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author fuqianzhong
 * @date 18/6/8
 */
public class ServiceRunner {
    public static void main(String[] args) {
        //启动服务
        ApplicationContext ctx = new ClassPathXmlApplicationContext("spring/rpc-server.xml");
        ctx.getBean("rpcServer");
    }
}
