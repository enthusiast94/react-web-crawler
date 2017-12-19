package com.manasb.reactwebcrawler.crawler;

import com.manasb.reactwebcrawler.crawler.domain.SiteMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class Crawler {

    private static final Logger log = LoggerFactory.getLogger(Crawler.class);
    private final ExecutorService executorService;
    private final Scraper scraper;
    private final List<CompletableFuture> pendingFutures = new ArrayList<>();
    private final CompletableFuture<SiteMap> overallProgressFuture = new CompletableFuture<>();
    private SiteMap siteMap;

    Crawler(ExecutorService executorService, Scraper scraper) {
        this.executorService = executorService;
        this.scraper = scraper;
    }

    public Future<SiteMap> crawl(URL baseUrl, int depth) {
        if (depth < 1) {
            return CompletableFuture.failedFuture(new Exception("depth must be > 0"));
        }

        siteMap = new SiteMap(baseUrl);
        String baseDomain = baseUrl.getHost();
        List<URL> allLinks = scrapeLinks(baseUrl);
        List<URL> internalLinks = allLinks.stream()
                .filter(isInternal(baseDomain))
                .collect(toList());

        siteMap.addNode(baseUrl, allLinks);

        for (int i = 0; i < internalLinks.size(); i++) {
            URL link = internalLinks.get(i);
                crawlRecursively(link, baseDomain, depth-1, i == internalLinks.size() - 1);
        }

        return overallProgressFuture;
    }

    private void crawlRecursively(URL url, String baseDomain, int depth, boolean isLast) {
        if (siteMap.hasLinkAlreadyBeenVisited(url) || depth == 0) {
            if (isLast) {
                waitForAllPendingFutures();
            }

            return;
        }

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            List<URL> links = scrapeLinks(url);
            siteMap.addNode(url, links);

            links.stream()
                    .filter(isInternal(baseDomain))
                    .forEach(link -> crawlRecursively(link, baseDomain, depth-1, isLast));
        }, executorService);

        pendingFutures.add(future);
    }

    private Predicate<URL> isInternal(String baseDomain) {
        return url -> url.getHost().equals(baseDomain);
    }

    private List<URL> scrapeLinks(URL url) {
        try {
            log.info("Scraping {}", url.toString());
            return scraper.scrapeLinks(url);
        } catch (IOException e) {
            log.error("There was a problem scraping URL [{}], so ignoring it: {}", url.toString(), e);
            return new ArrayList<>();
        }
    }

    private void waitForAllPendingFutures() {
        CompletableFuture[] pendingFuturesArray = pendingFutures.toArray(new CompletableFuture[pendingFutures.size()]);
        CompletableFuture.allOf(pendingFuturesArray).whenComplete((ignored, throwable) -> {
            if (throwable != null) {
                log.error("Error waiting for all pending futures: {}", throwable);
                overallProgressFuture.completeExceptionally(throwable);
            }

            overallProgressFuture.complete(siteMap);
        });
    }

    /**
     * For testing
     */
    public static void main(String[] args) throws MalformedURLException {
        Crawler crawler = null;
        try {
            crawler = new Crawler(Executors.newFixedThreadPool(10), new Scraper());
            SiteMap siteMap = crawler.crawl(new URL("http://fashiontap.com/"), 1).get();
            log.info("Done");
        } catch (ExecutionException | InterruptedException e) {
            log.error(e.getMessage());
        } finally {
            assert crawler != null;
            crawler.executorService.shutdownNow();
        }
    }
}
