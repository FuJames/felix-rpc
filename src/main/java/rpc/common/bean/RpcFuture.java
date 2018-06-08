package rpc.common.bean;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author fuqianzhong
 * @date 18/6/5
 * 获取服务器端的返回值,
 * 1. 客户端发出请求后,调用getResponse方法,等待服务端的方法
 * 1. Netty客户端获取到服务端的数据后,调用setResponse方法,填充Response并且唤醒等待线程
 */
public class RpcFuture {
    private RpcRequest request;

    private RpcResponse response;

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private boolean isDone = false;

    public RpcFuture(RpcRequest request) {
        this.request = request;
    }

    public RpcResponse getResponse() throws InterruptedException {
        if(response != null){
            return response;
        }

        lock.lock();
        try {
            long timeout = request.getTimeout();
            long timeoutLeft = timeout;
            long start = request.getCreateMillisTime();
            while (!isDone){
                condition.await(timeoutLeft, TimeUnit.MILLISECONDS);
                long timeoutPassed = System.currentTimeMillis() - start;
                if(this.isDone || (timeoutPassed >= timeout)){
                    break;
                }else {
                    timeoutLeft = timeout - timeoutPassed;
                }
            }
        } finally {
            lock.unlock();
        }
        if(!isDone){
            System.out.println("request timeout, current time:" + System.currentTimeMillis());
        }
        return response;
    }

    public void setResponse(RpcResponse response) {
        this.response = response;
        lock.lock();
        try {
            this.isDone = true;
            if(condition != null){
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

}
