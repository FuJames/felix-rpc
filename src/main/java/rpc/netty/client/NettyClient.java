package rpc.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import rpc.common.bean.RpcFuture;
import rpc.common.bean.RpcFuturePool;
import rpc.common.bean.RpcRequest;
import rpc.common.bean.RpcResponse;
import rpc.netty.codec.NettyDecoder;
import rpc.netty.codec.NettyEncoder;
import rpc.zk.registry.bean.ServerInfo;

/**
 * Created by fuqianzhong on 16/9/30.
 */
public class NettyClient implements Client{
    private ServerInfo server;
    private Channel channel;
    private EventLoopGroup workerGroup;


    public NettyClient(ServerInfo server){
        this.server = server;
        initClient();
    }

    private void initClient(){
        try{
            workerGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();//客户端使用Bootstrap类
            bootstrap.group(workerGroup).channel(NioSocketChannel.class)
                     .option(ChannelOption.TCP_NODELAY,true)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new NettyEncoder(RpcRequest.class))
                                    .addLast(new NettyDecoder(RpcResponse.class))
                                    .addLast(new NettyClientHandler());
                         }
                     });
            ChannelFuture channelFuture = bootstrap.connect(server.getIp(),server.getPort()).sync();//发起异步连接操作,没有连接成功之前会阻塞本身,但不会占用cpu
            System.out.println("[NettyClient] Connected to " + server.getIp()+":"+server.getPort());
            channel = channelFuture.channel();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean isValidate() {
        if (this.channel != null) {
            return this.channel.isActive();
        }
        return false;
    }

    public void close() {
        if (this.channel != null) {
            if (this.channel.isOpen()) {
                this.channel.close();
            }
        }
        if(workerGroup != null){
            workerGroup.shutdownGracefully();
        }
    }


    @Override
    public RpcResponse sendRequest(RpcRequest request) {
        RpcFuture future = new RpcFuture(request);
        RpcFuturePool.addFuture(request.getRequestId(), future);
        try {
            this.send(request);
            RpcResponse response = future.getResponse();
            return response;
        } catch (Exception e) {
        } finally {
            RpcFuturePool.removeFuture(request.getRequestId());
        }
        return null;
    }

    private void send(RpcRequest request) throws Exception {
        this.channel.writeAndFlush(request).sync();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NettyClient that = (NettyClient) o;

        if (server == null || that.server == null) {
            return false;
        }
        return server.equals(that.server);

    }

    @Override
    public int hashCode() {
        int result = server != null ? server.hashCode() : 0;
        result = 31 * result + (server != null ? server.getPort() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NettyClient:" + server;
    }

}
