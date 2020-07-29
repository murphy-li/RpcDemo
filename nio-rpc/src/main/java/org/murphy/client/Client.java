package org.murphy.client;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import service.DataService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    private String host;
    private int port;
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }

    /**
     * 获得代理对象
     * @param <T>
     * @return
     */
    public <T> T getProxyObject(Class<T> tClass){
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(DataService.class);
        //设置回调函数
        enhancer.setCallback(new MethodInterceptor(){
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                //注意这里的方法调用，不是用反射哦！！！
                try {
                    SocketChannel socketChannel = SocketChannel.open();
                    socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));
                    String className = tClass.getName();
                    String methodName = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Object [] params = objects;
                    String rpc = className + ":" + methodName + ":" + parameterTypes.toString() + params.toString();
                    socketChannel.write(ByteBuffer.wrap("Query Date".getBytes()));
                    socketChannel.shutdownOutput();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    socketChannel.read(byteBuffer);
                    socketChannel.close();
                    return new String(byteBuffer.array());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });

        //这里的creat方法就是正式创建代理类
        return (T)enhancer.create();
    }
}
