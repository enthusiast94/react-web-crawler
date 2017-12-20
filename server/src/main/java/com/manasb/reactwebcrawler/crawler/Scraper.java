package com.manasb.reactwebcrawler.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Scraper {

    public List<URL> scrapeLinks(URL url, URL baseUrl) throws IOException {
        Document document = Jsoup.connect(url.toString()).get();
        Elements urlElements = document.select("a[href]");

        Set<URL> urls = new HashSet<>();
        for (Element urlElement : urlElements) {
            String urlString = urlElement.attr("href");

            if (urlString.startsWith("/")) {
                urls.add(new URL(baseUrl.toString() + urlString));
            } else if (urlString.startsWith("http")) {
                urls.add(new URL(urlString));
            }
        }

        return new ArrayList<>(urls);
    }
}
