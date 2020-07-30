package org.murphy.server;

import org.murphy.service.DataService;
import org.murphy.service.DataServiceImpl;

import java.util.logging.Logger;

public class NettyServerMain {
    private static Logger logger = Logger.getLogger("Starter");

    public static void main(String [] agres) throws Exception {
        //EchoServerImpl类，实现了IEchoServer 接口
        NettyServer echoNettyServer =new NettyServer(8088);
        //将契约注册到注册中心，契约储存的Key：契约，Value：契约实现类。
        echoNettyServer.register(DataService.class, DataServiceImpl.class);
        //启动
        echoNettyServer.start();
    }
}
