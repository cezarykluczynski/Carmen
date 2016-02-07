package com.cezarykluczynski.carmen.util.network;

import groovy.util.logging.Log4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Log4j
public class HTTPJSONClientImpl implements HTTPClient<JSONObject> {

    private String ip;

    private Integer port;

    public HTTPJSONClientImpl(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
        createHttpClient();
    }

    @Override
    public JSONObject get(String url) throws HTTPRequestException {
        CloseableHttpClient httpClient = createHttpClient();
        HttpGet httpGet = new HttpGet(buildFullUrl(url));
        return executeHttpRequest(httpClient, httpGet);
    }

    @Override
    public JSONObject post(String url, Map<String, String> params) throws HTTPRequestException {
        CloseableHttpClient httpClient = createHttpClient();
        HttpPost httpPost = new HttpPost(buildFullUrl(url));
        httpPost.setParams(new BasicHttpParams());
        setHttpPostEntity(httpPost, params);
        return executeHttpRequest(httpClient, httpPost);
    }

    private void setHttpPostEntity(HttpPost httpPost, Map<String, String> params) throws HTTPRequestException {
        Iterator paramsIterator = params.entrySet().iterator();
        List<NameValuePair> urlParameters = new ArrayList<>();

        while(paramsIterator.hasNext()) {
            Map.Entry entry = (Map.Entry) paramsIterator.next();
            urlParameters.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(urlParameters));
        } catch(UnsupportedEncodingException e) {
            throw new HTTPRequestException(e);
        }
    }

    private JSONObject executeHttpRequest(CloseableHttpClient httpClient, HttpUriRequest httpPost) throws HTTPRequestException{
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            return entity != null ? new JSONObject(EntityUtils.toString(entity)) : null;
        } catch(Throwable e) {
            throw new HTTPRequestException(e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
            }
        }
    }


    private CloseableHttpClient createHttpClient() {
        return HttpClients.createDefault();
    }

    private String buildFullUrl(String relativeUrl) {
        return "http://" + ip + ":" + port + "/" + relativeUrl;
    }

}
