package org.murphy.service;

public class DataServiceImpl implements DataService{
    public Object getData(Object o) {
        return o.toString() + "，服务端偷偷做了处理";
    }
}
