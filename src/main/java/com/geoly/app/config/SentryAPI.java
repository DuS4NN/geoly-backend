package com.geoly.app.config;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentryAPI {


    @Value("${sentry.url}")
    private String url;

    public SentryAPI(){
        Sentry.init(url);
    }

    @Bean
    public SentryClient sentryClient(){
        return SentryClientFactory.sentryClient();
    }
}
