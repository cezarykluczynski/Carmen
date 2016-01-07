package com.cezarykluczynski.carmen.util.network;

import org.json.JSONObject;

import java.util.Map;

public interface HTTPClient<T> {

    T get(String url) throws HTTPRequestException;

    T post(String url, Map<String, String> params) throws HTTPRequestException;

}
