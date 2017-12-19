package com.manasb.reactwebcrawler.api;

import com.manasb.reactwebcrawler.crawler.Crawler;
import com.manasb.reactwebcrawler.crawler.CrawlerFactory;
import com.manasb.reactwebcrawler.crawler.SiteMapJsonFormatter;
import com.manasb.reactwebcrawler.crawler.domain.SiteMap;
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

    public CrawlerResource(CrawlerFactory crawlerFactory, SiteMapJsonFormatter siteMapJsonFormatter) {
        this.crawlerFactory = crawlerFactory;
        this.siteMapJsonFormatter = siteMapJsonFormatter;
    }

    @Path("/crawl")
    @GET
    public Response crawl(@QueryParam("url") String url, @QueryParam("depth") int depth) {

        if (url == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("url is a required parameter").build();
        }

        URL netUrl;
        try {
            netUrl = new URL(url);
        } catch (MalformedURLException e) {
            log.error(e.getMessage(), e);
            return Response.status(Response.Status.BAD_REQUEST).entity("Provided URL is malformed").build();
        }

        Crawler crawler = crawlerFactory.newCrawler();
        try {
            SiteMap siteMap = crawler.crawl(netUrl, depth).get();
            return Response.ok().entity(siteMapJsonFormatter.format(siteMap)).build();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
