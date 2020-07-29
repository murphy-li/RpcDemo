package org.murphy.provider;

import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
    int port;
    private ApplicationContext applicationContext;
    public Server(int port, ApplicationContext applicationContext){
        this.port = port;
        this.applicationContext = applicationContext;
    }
    public void startService() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(this.port));
        while(true){
            Socket socket = serverSocket.accept();
            AsyncRPC asyncRPC = new AsyncRPC(socket, applicationContext);
            // execute 没有返回值
            threadPoolExecutor.execute(asyncRPC);
            // submit有返回值
//            threadPoolExecutor.submit(asyncRPC);
//            new Thread(asyncRPC).start();
        }

    }
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 5, 60, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
}
