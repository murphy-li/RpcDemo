package org.murphy.server;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        Server server = new Server(8888);
        server.start();
    }
}
