package com.manasb.reactwebcrawler.crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.manasb.reactwebcrawler.crawler.domain.SiteMap;

import java.net.URL;

public class SiteMapJsonFormatter {

    public JsonArray format(SiteMap siteMap) {
        JsonArray links = new JsonArray();
        siteMap.nodesByUrl.values().forEach(node -> {
            node.links.forEach(link -> {
                JsonObject object = new JsonObject();
                object.addProperty("source", formatUrl(node.url, siteMap.baseUrl));
                object.addProperty("target", formatUrl(link, siteMap.baseUrl));
                links.add(object);
            });
        });
        return links;
    }

    private String formatUrl(URL url, URL baseUrl) {
        if (url.toString().equals(baseUrl.toString()) || !url.toString().contains(baseUrl.toString())) {
            return url.toString();
        } else {
            return url.getPath();
        }
    }
}
