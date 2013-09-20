package com.launchkey.sdk.http;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class HttpClientFactory {
    private int maxConnections;

    public HttpClient createClient() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, this.maxConnections);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean();
        HttpHost host = new HttpHost("api.launchkey.com");
        connPerRoute.setMaxForRoute(new HttpRoute(host), 200);
        ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);

        ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(params, schemeRegistry);

        return new DefaultHttpClient(threadSafeClientConnManager, params);
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }
}
