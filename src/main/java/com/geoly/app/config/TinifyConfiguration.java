package com.geoly.app.config;

import com.tinify.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class TinifyConfiguration {

    @Value("${tinify.key}")
    private String key;

    @PostConstruct
    public void postConstruct(){
        Tinify.setKey(key);
    }
}
