package org.murphy.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.murphy.service.DataService;

public class InterfaceProxy {

    /**
     * 获得代理对象
     * @param <T>
     * @return
     */
    public static <T> T getProxyObject(Class<T> tClass, MethodInterceptor methodInterceptor){
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(tClass);
        //设置回调函数
        enhancer.setCallback(methodInterceptor);

        //这里的creat方法就是正式创建代理类
        return (T)enhancer.create();
    }
}
