package org.murphy.service.impl;

import org.murphy.service.DataService;

public class DataServiceImpl implements DataService {
    public Object getData(Object o) {
        System.out.println("收到消费者传入的参数：" + o);
        return "服务端消息";
    }
}
