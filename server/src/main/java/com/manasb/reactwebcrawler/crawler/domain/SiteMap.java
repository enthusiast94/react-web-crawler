package com.manasb.reactwebcrawler.crawler.domain;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SiteMap {

    public final URL baseUrl;
    public final Map<String, Node> nodesByUrl = new ConcurrentHashMap<>();

    public SiteMap(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void addNode(URL url, List<URL> links) {
        nodesByUrl.put(url.toString(), new Node(url, links));
    }

    public Node getNode(URL url) {
        return nodesByUrl.get(url.toString());
    }

    public boolean hasLinkAlreadyBeenVisited(URL link) {
        return nodesByUrl.containsKey(link.toString());
    }
}
