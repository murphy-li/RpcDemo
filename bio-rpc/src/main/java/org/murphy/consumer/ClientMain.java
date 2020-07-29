package org.murphy.consumer;

import org.murphy.service.DataService;

public class ClientMain {
    public static void main(String[] args) {
        Client client = new Client("localhost", 8888);
        DataService proxyObject = client.getProxyObject(DataService.class);
        System.out.println(proxyObject.getData(new Integer(1234567890)));
    }
}
