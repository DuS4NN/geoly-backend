package com.geoly.app.config;

import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PusherConfiguration {

    @Value("${pusher.app.id}")
    private String appId;
    @Value("${pusher.key}")
    private String key;
    @Value("${pusher.secret}")
    private String secret;
    @Value("${pusher.cluster}")
    private String cluster;

    @Bean
    public Pusher getPusher(){
        Pusher pusher = new Pusher(appId, key, secret);
        pusher.setCluster(cluster);
        pusher.setEncrypted(true);

        return pusher;
    }
}
