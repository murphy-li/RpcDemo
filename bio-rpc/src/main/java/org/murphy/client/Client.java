package org.murphy.client;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import service.DataService;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;

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
//                return methodProxy.invokeSuper(o, objects);
                Socket socket = null;
                ObjectOutputStream objectOutputStream = null;
                ObjectInputStream objectInputStream = null;
                try{
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(host, port));
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeUTF(tClass.getName());
                    objectOutputStream.writeUTF(method.getName());
                    objectOutputStream.writeObject(method.getParameterTypes());
                    objectOutputStream.writeObject(objects);
                    objectOutputStream.flush();
                    objectInputStream = new ObjectInputStream(socket.getInputStream());
                    return objectInputStream.readObject();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(socket != null){
                        socket.close();
                    }
                    if(objectInputStream != null){
                        objectInputStream.close();
                    }
                    if(objectOutputStream != null){
                        objectOutputStream.close();
                    }
                }
                return null;
            }
        });

        //这里的creat方法就是正式创建代理类
        return (T)enhancer.create();
    }
}
