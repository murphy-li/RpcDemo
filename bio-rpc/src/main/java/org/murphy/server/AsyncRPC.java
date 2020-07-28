package org.murphy.server;

import org.springframework.context.ApplicationContext;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AsyncRPC implements Runnable{
    private Socket socket;
    private ApplicationContext applicationContext;
    public AsyncRPC(Socket socket, ApplicationContext applicationContext){
        this.socket = socket;
        this.applicationContext = applicationContext;
    }
    @Override
    public void run() {
        ObjectOutputStream objectOutputStream = null;
        ObjectInputStream objectInputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(this.socket.getOutputStream());
            objectInputStream = new ObjectInputStream(this.socket.getInputStream());
            String className = objectInputStream.readUTF();
            String methodName = objectInputStream.readUTF();
            Class<?> [] parameterTypes = (Class<?> [])objectInputStream.readObject();
            Object[] parameters = (Object [])objectInputStream.readObject();
            Object bean = applicationContext.getBean(Class.forName(className));
            Object res = bean.getClass().getMethod(methodName, parameterTypes).invoke(bean, parameters);
            objectOutputStream.writeObject(res);
            objectOutputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(socket != null){
                socket = null;
            }
            if(objectInputStream != null){
                objectInputStream = null;
            }
            if(objectOutputStream != null){
                objectOutputStream = null;
            }
        }
    }
}
