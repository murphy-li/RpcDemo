package org.murphy.service.impl;

import org.springframework.stereotype.Component;
import org.murphy.service.DataService;
@Component
public class DataServiceImpl implements DataService {
    @Override
    public Object getData(Object o) {
        return o.toString() + " emmmmmmm.......";
    }
}
