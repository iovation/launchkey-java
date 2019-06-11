package com.iovation.launchkey.sdk.integration.managers.kobiton.transport;

public class GenericResponse {

    private final int statusCode;
    private final String body;

    public GenericResponse(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }
}
