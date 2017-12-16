package com.manasb.reactwebcrawler.bootstrap;

import com.manasb.reactwebcrawler.api.PingPongResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBootstrapper {

    @Bean
    public ResourceConfig resourceConfig() {
        return new JerseyConfig();
    }

    @Bean
    public PingPongResource helloResource() {
        return new PingPongResource();
    }
}
