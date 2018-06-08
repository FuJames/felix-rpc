package rpc.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rpc.common.bean.RpcRequest;
import rpc.common.bean.RpcResponse;
import rpc.protocal.ServiceFactory;

import java.lang.reflect.Method;

/**
 * Created by fuqianzhong on 16/9/30.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        System.out.println("Server received : " + request);
        RpcResponse response = new RpcResponse();
        response.setRequestId(request.getRequestId());
        response.setSerialize(request.getSerialize());
        try {
            Object serviceBean = ServiceFactory.getServiceBean(request.getServiceKey());
            Class<?> serviceClass = serviceBean.getClass();
            Method method = serviceClass.getMethod(request.getMethodName(),request.getParameterTypes());
            Object result = method.invoke(serviceBean,request.getParameters());
            System.out.println("Server invoke result : " + result);
            response.setResult(result);
        } catch (Throwable t) {
            t.printStackTrace();
            response.setError(t);
        }
        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context)throws Exception{
        context.flush();
    }

    /**
     * 当发送异常时,关闭资源
     * @param context
     * @param throwable
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable throwable)throws Exception{
        throwable.printStackTrace();
        context.close();
    }
}
