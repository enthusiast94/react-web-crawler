package com.manasb.reactwebcrawler.crawler;

import java.util.concurrent.ExecutorService;

public class CrawlerFactory {

    private final ExecutorService executorService;
    private final Scraper scraper;

    public CrawlerFactory(ExecutorService executorService, Scraper scraper) {
        this.executorService = executorService;
        this.scraper = scraper;
    }

    public Crawler newCrawler() {
        return new Crawler(executorService, scraper);
    }
}
