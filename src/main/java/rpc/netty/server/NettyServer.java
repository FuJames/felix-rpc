package rpc.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import rpc.common.bean.RpcRequest;
import rpc.common.bean.RpcResponse;
import rpc.netty.codec.NettyDecoder;
import rpc.netty.codec.NettyEncoder;

/**
 * Created by fuqianzhong on 16/9/30.
 * Netty Nio Server
 * 1. 一切从ServerBootStrap开始
 * 2. 设置Reactor线程池,bossGroup用来处理客户端的连接(acceptor),workerGroup用来处理ChannelSocket的读写(worker thread)
 */
public class NettyServer {
    public void doStart(final int port){
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            bootstrap.group(bossGroup,workerGroup)
                     .channel(NioServerSocketChannel.class)
                     .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000)
                     .childHandler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel socketChannel) throws Exception {
                             socketChannel.pipeline()
                                     .addLast(new NettyEncoder(RpcResponse.class))//encoder将response转化为字节流
                                     .addLast(new NettyDecoder(RpcRequest.class))//decoder将字节流转化为request
                                     .addLast(new NettyServerHandler());
                         }
                     });
            ChannelFuture cf = bootstrap.bind(port).sync();
            System.out.println("NettyServer started and listen on " + cf.channel().localAddress());
            cf.channel().closeFuture().sync();//同步阻塞的方式等待服务器端口关闭
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();//释放线程池资源
            workerGroup.shutdownGracefully();
        }
    }

}
