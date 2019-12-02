package com.iovation.launchkey.sdk.example.springmvc;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lk")
public class LaunchkeySdkConfig {
    private String baseUrl;
    private String organizationId;
    private String directoryId;
    private String serviceId;
    private String privateKeyLocation;
    private String externalUrl;
    private boolean useAdvancedWebhook = true;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDirectoryId() {
        return directoryId;
    }

    public void setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
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

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public boolean useAdvancedWebhook() {
        return useAdvancedWebhook;
    }

    public void setUseAdvancedWebhook(boolean useAdvancedWebhook) {
        this.useAdvancedWebhook = useAdvancedWebhook;
    }
}
