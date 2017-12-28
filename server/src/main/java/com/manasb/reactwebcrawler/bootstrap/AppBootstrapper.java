package com.manasb.reactwebcrawler.bootstrap;

import com.manasb.reactwebcrawler.api.CrawlerResource;
import com.manasb.reactwebcrawler.api.PingPongResource;
import com.manasb.reactwebcrawler.crawler.CrawlerFactory;
import com.manasb.reactwebcrawler.crawler.Scraper;
import com.manasb.reactwebcrawler.crawler.SiteMapJsonFormatter;
import com.manasb.reactwebcrawler.util.HttpResponseFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class AppBootstrapper {

    @Bean
    public ResourceConfig resourceConfig(PingPongResource pingPongResource, CrawlerResource crawlerResource) {
        return new JerseyConfig(pingPongResource, crawlerResource);
    }

    @Bean(destroyMethod = "shutdownNow")
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean
    public PingPongResource pingPongResource() {
        return new PingPongResource();
    }

    @Bean
    public CrawlerResource crawlerResource(ExecutorService executorService) {
        return new CrawlerResource(
                new CrawlerFactory(executorService, new Scraper()),
                new SiteMapJsonFormatter(),
                new HttpResponseFactory());
    }
}
