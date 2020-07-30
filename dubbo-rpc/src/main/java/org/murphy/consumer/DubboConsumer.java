package org.murphy.consumer;


import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.murphy.service.DataService;

public class DubboConsumer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "remote-consumer.xml" });
        context.start();
        DataService service = (DataService) context.getBean("dataService");
        System.out.println(service.getData("消费者传入的参数"));
        context.close();
    }
}
