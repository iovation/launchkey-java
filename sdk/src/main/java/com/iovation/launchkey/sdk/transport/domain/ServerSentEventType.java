package com.iovation.launchkey.sdk.transport.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServerSentEventType {
    public final static String AUTHORIZATION_RESPONSE_WEBHOOK = "AUTHORIZATION_RESPONSE";
    public final static String DEVICE_LINK_COMPLETION_WEBHOOK = "DEVICE_LINK_COMPLETION";
    private final String type;


    @JsonCreator
    public ServerSentEventType(@JsonProperty("type") String type) {
        this.type = type == null ? AUTHORIZATION_RESPONSE_WEBHOOK : type;
    }

    public String getType() {
        return type;
    }
}
