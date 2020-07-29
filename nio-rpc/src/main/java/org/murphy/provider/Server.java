package org.murphy.provider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    int port;
    Selector selector;
    public Server(int port){
        this.port = port;
    }
    public void init() throws IOException {
        ServerSocketChannel ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.configureBlocking(false);
            ssc.bind(new InetSocketAddress(port));
            selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("NioServer started ......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start() throws IOException {
        this.init();
        while(true){
            try{
                int numOfEvents = selector.select();
                if(numOfEvents > 0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();
                        if (selectionKey.isAcceptable()) {
                            System.out.println("-- -- -- 处理Acceptable事件");
                            ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                            SocketChannel sc = ssc.accept();
                            sc.configureBlocking(false);
                            sc.register(selector, SelectionKey.OP_READ);
                        } else if (selectionKey.isReadable()) {
                            System.out.println("-- -- -- 处理Readable事件");
                            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                            byteBuffer.flip();
                            SocketChannel sc = (SocketChannel) selectionKey.channel();
                            sc.read(byteBuffer);
                            System.out.println("客户端( " + sc.getRemoteAddress() + " ) 请求 ： " + new String(byteBuffer.array()));
                            sc.register(selector, SelectionKey.OP_WRITE);
                            // 同时支持read和write
                            selectionKey.interestOps(SelectionKey.OP_WRITE|SelectionKey.OP_READ);
                            res.put(sc, "1234567890");
                            if(selectionKey.isWritable()){
                                System.out.println("真鸡儿操蛋");
                            }else{
                                System.out.println("更加操蛋了");
                            }
                        } else if (selectionKey.isWritable()) {
                            System.out.println("-- -- -- 处理Writable事件");
//                            String response = "服务端响应 ： " + Calendar.getInstance().toString();
                            String response = (String) res.get((SocketChannel) selectionKey.channel());
                            ByteBuffer byteBuffer = ByteBuffer.wrap(response.getBytes());
                            SocketChannel sc = (SocketChannel) selectionKey.channel();
                            sc.write(byteBuffer);
                            sc.close();
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    Map<SocketChannel, Object> res = new ConcurrentHashMap<>();
}
