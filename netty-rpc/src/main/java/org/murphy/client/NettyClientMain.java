package org.murphy.client;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.murphy.RpcParameter;
import org.murphy.callback.INettyCallBack;
import org.murphy.proxy.InterfaceProxy;
import org.murphy.service.DataService;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Logger;

public class NettyClientMain {
    private static java.util.logging.Logger logger = Logger.getLogger("Starter");

    public static void main(String [] agres){
        final String host = "127.0.0.1";
        final int port = 8088;
        InetSocketAddress inetSocketAddress=new InetSocketAddress(host, port);
        Class<DataService> clazz = DataService.class;
        DataService userService= InterfaceProxy.getProxyObject(clazz, new MethodInterceptor() {
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                NettyClient nettyClient =new NettyClient(inetSocketAddress);
                RpcParameter rpcParameter=new RpcParameter();
                rpcParameter.setClzName(clazz.getName());
                rpcParameter.setMethodName(method.getName());
                rpcParameter.setParamTypes(method.getParameterTypes());
                rpcParameter.setArguments(objects);
                nettyClient.setRpcParameter(rpcParameter);
                nettyClient.setNettyCallBack(new INettyCallBack() {
                    @Override
                    public void calllBack(String toString) {
                        System.out.println("回调函数收到字符串：" + toString);
                    }
                });
                nettyClient.start();
                return null;
            }
        });
        Object res = userService.getData("1");
        System.out.println(res);
    }
}
