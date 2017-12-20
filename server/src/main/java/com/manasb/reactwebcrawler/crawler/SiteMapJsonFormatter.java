package com.manasb.reactwebcrawler.crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.manasb.reactwebcrawler.crawler.domain.Node;
import com.manasb.reactwebcrawler.crawler.domain.SiteMap;
import jersey.repackaged.com.google.common.collect.Lists;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class SiteMapJsonFormatter {

    public String format(SiteMap siteMap) {
        JsonArray links = new JsonArray();
        siteMap.nodesByUrl.values().forEach(node -> {
            node.links.forEach(link -> {
                JsonObject object = new JsonObject();
                object.addProperty("source", node.url.toString());
                object.addProperty("target", link.toString());
                links.add(object);
            });
        });
        return links.toString();
    }
}
