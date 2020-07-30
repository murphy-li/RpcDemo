package org.murphy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyServer {

    private final Integer port;

    public NettyServer(int port){
        this.port=port;
    }

    public void start() throws Exception {
        System.out.println("启动中");

        // 基于选择器的通道实现
        NioEventLoopGroup nioEventGroup=new NioEventLoopGroup();
        try {
            // ServerChannel的快速启动
            ServerBootstrap bootstrap=new ServerBootstrap();
            // 指定用于父级（acceptor）和子级（client）的EventLoopGroup
            bootstrap.group(nioEventGroup)
                    //指定使用 NIO 的传输 Channel
                    .channel(NioServerSocketChannel.class)
                    //.设置 socket 地址使用所选的端口
                    .localAddress(new InetSocketAddress(port))
                    //添加 ServerHandler 到 Channel 的 ChannelPipeline
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            // 末尾添加
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            //绑定的服务器;sync 等待服务器关闭
            ChannelFuture future=bootstrap.bind().sync();
            //关闭 channel 和 块，直到它被关闭
            future.channel().closeFuture().sync();
        }finally {
            //关机的 EventLoopGroup，释放所有资源。
            nioEventGroup.shutdownGracefully().sync();
        }
    }

    /**
     * 注册类到具体的实现
     * @param serviceInterface
     * @param impl
     */

    public static void register(Class serviceInterface, Class impl) {
        serviceRegistry.put(serviceInterface.getName(), impl);
    }
    public static Map<String, Class<?>> serviceRegistry = new ConcurrentHashMap<String, Class<?>>();
}
