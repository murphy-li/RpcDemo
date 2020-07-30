package org.murphy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.murphy.RpcParameter;
import org.murphy.callback.INettyCallBack;

import java.net.InetSocketAddress;

public class NettyClient {
    private InetSocketAddress inetSocketAddress;
    private INettyCallBack nettyCallBack;
    private RpcParameter rpcParameter;

    public NettyClient(InetSocketAddress inetSocketAddress){
        this.inetSocketAddress = inetSocketAddress;
    }
    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(inetSocketAddress)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            NettyClientHandler nettyClientHandler = new NettyClientHandler();
                            nettyClientHandler.setRpcParameter(rpcParameter);
                            nettyClientHandler.setNettyCallBack(nettyCallBack);;
                            socketChannel.pipeline().addLast(nettyClientHandler);
                        }
                    });

            ChannelFuture future = b.connect().sync();
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public void setRpcParameter(RpcParameter rpcParameter) {
        this.rpcParameter = rpcParameter;
    }

    public void setNettyCallBack(INettyCallBack nettyCallBack) {
        this.nettyCallBack = nettyCallBack;
    }
}