package com.manasb.reactwebcrawler.bootstrap;

import com.manasb.reactwebcrawler.api.PingPongResource;
import org.glassfish.jersey.server.ResourceConfig;

import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(PingPongResource.class);
    }
}
