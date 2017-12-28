package com.manasb.reactwebcrawler.api;

import com.manasb.reactwebcrawler.crawler.Crawler;
import com.manasb.reactwebcrawler.crawler.CrawlerFactory;
import com.manasb.reactwebcrawler.crawler.SiteMapJsonFormatter;
import com.manasb.reactwebcrawler.crawler.domain.SiteMap;
import com.manasb.reactwebcrawler.util.HttpResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

@Path("/")
public class CrawlerResource {

    private static final Logger log = LoggerFactory.getLogger(CrawlerResource.class);

    private final CrawlerFactory crawlerFactory;
    private final SiteMapJsonFormatter siteMapJsonFormatter;
    private final HttpResponseFactory httpResponseFactory;

    public CrawlerResource(CrawlerFactory crawlerFactory,
                           SiteMapJsonFormatter siteMapJsonFormatter,
                           HttpResponseFactory httpResponseFactory) {
        this.crawlerFactory = crawlerFactory;
        this.siteMapJsonFormatter = siteMapJsonFormatter;
        this.httpResponseFactory = httpResponseFactory;
    }

    @Path("/crawl")
    @GET
    public Response crawl(@QueryParam("url") String url, @QueryParam("depth") int depth) {

        if (url == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(httpResponseFactory.createErrorMessage("url is a " +
                    "required parameter")).build();
        }

        URL netUrl;
        try {
            netUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity(httpResponseFactory.createErrorMessage(String.format(
                    "url [%s] is malformed", url))).build();
        }

        if (depth < 1 || depth > 3) {
            return Response.status(Response.Status.BAD_REQUEST).entity(httpResponseFactory.createErrorMessage(String.format(
                    "depth must be > 0 and < 4 but " +
                    "was %s", depth))).build();
        }

        Crawler crawler = crawlerFactory.newCrawler(netUrl, depth);
        try {
            log.info("Started crawling [{}]", url);
            SiteMap siteMap = crawler.crawl();
            log.info("Finished crawling [{}]", url);
            return Response.ok().entity(httpResponseFactory.createOkMessage(siteMapJsonFormatter.format(siteMap, depth))).build();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
