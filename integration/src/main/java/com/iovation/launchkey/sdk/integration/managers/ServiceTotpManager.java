package com.iovation.launchkey.sdk.integration.managers;


import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServiceTotpManager {
    private final DirectoryServiceManager directoryServiceManager;
    private boolean currentVerifyUserTotpResponse;

    @Inject
    public ServiceTotpManager(DirectoryServiceManager directoryServiceManager) {
        this.directoryServiceManager = directoryServiceManager;
    }

    public void verifyUserTotpCode(String userId, String totpCode) throws Throwable {
        currentVerifyUserTotpResponse = directoryServiceManager.getServiceClient().verifyTotp(userId, totpCode);
    }

    public Boolean getCurrentVerifyUserTotpResponse() {
        return currentVerifyUserTotpResponse;
    }
}
