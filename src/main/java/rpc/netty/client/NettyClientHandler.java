package rpc.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import rpc.common.bean.RpcFuturePool;
import rpc.common.bean.RpcResponse;

/**
 * Created by fuqianzhong on 16/9/30.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {
    /**
     * 当client与server连接成功后调用
     * @param context
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext context)throws Exception{
    }

    /**
     * client从channel中读取服务器返回的内容
     * @param context
     * @param response
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, RpcResponse response) throws Exception {
        RpcFuturePool.reciveResponse(response);
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
