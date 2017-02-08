package com.launchkey.example.springmvc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lk")
public class LaunchkeySdkConfig {
    private String baseUrl;
    private String serviceId;
    private String privateKeyLocation;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setPrivateKeyLocation(String privateKeyLocation) {
        this.privateKeyLocation = privateKeyLocation;
    }

    public String getPrivateKeyLocation() {
        return privateKeyLocation;
    }
}
