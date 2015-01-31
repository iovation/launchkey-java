package com.launchkey.sdk.http;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientFactory {
    private Integer maxConnections;

    public HttpClient createClient() {
        HttpClientBuilder builder = HttpClientBuilder.create();
        if (maxConnections != null) {
            builder.setMaxConnPerRoute(maxConnections);
        }

        return builder.build();
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }
}
