package com.iovation.launchkey.sdk.integration.managers.kobiton.transport;

import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.httpurlconnection.HttpURLConnectionRequest;

public class RequestFactory {
    public GenericRequest.Builder getDefaultRequestBuilder() {
        return new HttpURLConnectionRequest.Builder();
    }
}
