package com.manasb.reactwebcrawler.crawler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.manasb.reactwebcrawler.crawler.domain.Node;
import com.manasb.reactwebcrawler.crawler.domain.SiteMap;

import java.net.URL;

public class SiteMapJsonFormatter {

    public JsonObject format(SiteMap siteMap, int depth) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", siteMap.baseUrl.toString());
        jsonObject.add("children", getChildrenRecursively(siteMap.baseUrl, siteMap, depth));
        return jsonObject;
    }

    private JsonArray getChildrenRecursively(URL url, SiteMap siteMap, int depth) {
        Node node = siteMap.getNode(url);
        if (node == null || depth == 0) {
            return new JsonArray();
        }

        JsonArray children = new JsonArray();
        node.links.forEach(link -> {
            JsonObject child = new JsonObject();
            child.addProperty("name", formatUrl(link, siteMap.baseUrl));
            child.add("children", getChildrenRecursively(link, siteMap, depth-1));
            children.add(child);
        });

        return children;
    }

    private String formatUrl(URL url, URL baseUrl) {
        if (url.toString().equals(baseUrl.toString()) || !url.toString().contains(baseUrl.toString())) {
            return url.toString();
        } else {
            return url.getPath();
        }
    }
}
