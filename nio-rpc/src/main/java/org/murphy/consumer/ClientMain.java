package org.murphy.consumer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientMain {
        //这里定义协议头
        public static final String providerName = "DataService#getData#";

        public static void main(String[] args) throws  Exception{
            // 创建一个消费者
//            Client customer = new Client();
            new Thread() {
                @Override
                public void run() {
                    try {
                        SocketChannel socketChannel = SocketChannel.open();
                        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8888));

                        socketChannel.write(ByteBuffer.wrap("Query Date".getBytes()));
                        socketChannel.shutdownOutput();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        System.out.println(new String(byteBuffer.array()));
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }.start();
        }
}
