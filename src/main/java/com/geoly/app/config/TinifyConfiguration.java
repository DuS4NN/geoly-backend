package com.geoly.app.config;

import com.tinify.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TinifyConfiguration {

    @Value("${tinify.key}")
    private String key;

    public TinifyConfiguration(){
        Tinify.setKey(key);
    }
}
