package com.manasb.reactwebcrawler.util;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseFactory {

    private final Gson gson = new Gson();

    public String createOkMessage(Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        return gson.toJson(map);
    }

    public String createErrorMessage(String error) {
        Map<String, String> map = new HashMap<>();
        map.put("error", error);
        return gson.toJson(map);
    }
}
