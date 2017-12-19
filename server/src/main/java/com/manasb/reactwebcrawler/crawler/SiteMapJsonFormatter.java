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

    private final Set<String> alreadyIncludedLinks = new HashSet<>();

    public String format(SiteMap siteMap) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", siteMap.baseUrl.toString());
        Node node = siteMap.getNode(siteMap.baseUrl);
        alreadyIncludedLinks.add(siteMap.baseUrl.toString());
        JsonArray children = new JsonArray(node.links.size());

        node.links.forEach(link -> {
            JsonObject object = new JsonObject();
            object.addProperty("name", link.toString());
            alreadyIncludedLinks.add(link.toString());
            object.add("children", getChildrenRecursively(link, siteMap));
            children.add(object);
        });

        jsonObject.add("children", children);

        return jsonObject.toString();
    }

    private JsonArray getChildrenRecursively(URL url, SiteMap siteMap) {
        Node node = siteMap.getNode(url);
        JsonArray children = new JsonArray();

        if (node == null) {
            return children;
        }

        node.links.forEach(link -> {
            JsonObject object = new JsonObject();
            object.addProperty("name", link.toString());
            if (!alreadyIncludedLinks.contains(url.toString())) {
                alreadyIncludedLinks.add(link.toString());
                object.add("children", getChildrenRecursively(link, siteMap));
            } else {
                object.add("children", new JsonArray());
            }
            children.add(object);
        });


        return children;
    }


    /**
     * For testing
     */
    public static void main(String[] args) throws MalformedURLException {
        SiteMapJsonFormatter formatter = new SiteMapJsonFormatter();
        SiteMap siteMap = new SiteMap(new URL("http://www.google.com"));
        siteMap.addNode(new URL("http://www.google.com"), Lists.newArrayList(
                new URL("http://www.a.com"),
                new URL("http://www.b.com")
        ));
        siteMap.addNode(new URL("http://www.a.com"), Lists.newArrayList(
                new URL("http://www.google.com")
        ));
        System.out.println(formatter.format(siteMap));
    }
}
