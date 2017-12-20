package com.manasb.reactwebcrawler.crawler;

import com.manasb.reactwebcrawler.crawler.domain.SiteMap;
import com.manasb.reactwebcrawler.crawler.domain.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;

public class Crawler {

    private static final Logger log = LoggerFactory.getLogger(Crawler.class);

    private final ExecutorService executorService;
    private final Scraper scraper;
    private final URL baseUrl;
    private final String baseDomain;
    private final int depth;
    private final BlockingQueue<Task> pendingTasks = new LinkedBlockingQueue<>();
    private final SiteMap siteMap;

    Crawler(ExecutorService executorService, Scraper scraper, URL baseUrl, int depth)
            throws IllegalArgumentException {
        this.executorService = executorService;
        this.scraper = scraper;
        this.baseUrl = baseUrl;
        this.depth = depth;

        siteMap = new SiteMap(baseUrl);
        baseDomain = baseUrl.getHost();

        if (depth < 1) {
            throw new IllegalArgumentException("depth must be > 0");
        }
    }

    public Future<SiteMap> start() {
        crawl(baseUrl, depth);
        return CompletableFuture.completedFuture(siteMap);
    }

    private void crawl(URL url, int depth) {
        if (siteMap.hasLinkAlreadyBeenVisited(url) || depth == 0) {
            return;
        }

        List<URL> allLinks = scrapeLinks(url);
        siteMap.addNode(url, allLinks);

        allLinks.stream()
                .filter(isInternal(baseDomain))
                .forEach(link -> crawl(link, depth - 1));
    }

    private Predicate<URL> isInternal(String baseDomain) {
        return url -> url.getHost().equals(baseDomain);
    }

    private List<URL> scrapeLinks(URL url) {
        try {
            log.info("Scraping {}", url.toString());
            return scraper.scrapeLinks(url, baseUrl);
        } catch (IOException e) {
            log.error("There was a problem scraping URL [{}], so ignoring it: {}", url.toString(), e);
            return new ArrayList<>();
        }
    }

    /**
     * For testing
     */
    public static void main(String[] args) throws MalformedURLException {
        Crawler crawler = null;
        try {
            crawler = new Crawler(Executors.newFixedThreadPool(10), new Scraper(),
                    new URL("http://localhost:8080/"), 1);
            SiteMap siteMap = crawler.start().get();
            log.info("Done");
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            assert crawler != null;
            crawler.executorService.shutdownNow();
        }
    }
}
