package com.geoly.app.config;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.SentryClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SentryAPI {

    public SentryAPI(){
        Sentry.init("https://9abf2d4b51ca44328a41622d49247e21@o421143.ingest.sentry.io/5340459");
    }

    @Bean
    public SentryClient sentryClient(){
        return SentryClientFactory.sentryClient();
    }
}
