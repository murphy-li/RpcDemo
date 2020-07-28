package org.murphy.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;

@SpringBootConfiguration
@ComponentScan(basePackages = {"org.murphy.service", "org.murphy.server"})
public class ServerMain {
    public static void main(String[] args) throws IOException {
        ApplicationContext applicationContext = SpringApplication.run(ServerMain.class);
        Server bean = new Server(8888, applicationContext);
        bean.startService();
    }
}
