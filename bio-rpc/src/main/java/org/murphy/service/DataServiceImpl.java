package org.murphy.service;


import org.springframework.stereotype.Component;
import service.DataService;

@Component
public class DataServiceImpl implements DataService {
    public Object getData(Object o) {
        int value = (int) o;
        int sum = 0;
        while(value > 0){
            sum = sum * 10 + value % 10;
            value /= 10;
        }
        return sum;
    }
}
