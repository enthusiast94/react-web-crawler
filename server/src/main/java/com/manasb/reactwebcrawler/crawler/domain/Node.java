package com.manasb.reactwebcrawler.crawler.domain;

import java.net.URL;
import java.util.List;

public class Node {
    public URL url;
    public List<URL> links;

    public Node(URL url, List<URL> links) {
        this.url = url;
        this.links = links;
    }
}
