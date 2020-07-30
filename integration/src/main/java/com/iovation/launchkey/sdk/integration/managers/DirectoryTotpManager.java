package com.iovation.launchkey.sdk.integration.managers;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.iovation.launchkey.sdk.domain.DirectoryUserTotp;
import com.iovation.launchkey.sdk.integration.Utils;
import io.cucumber.java.After;
import org.apache.commons.codec.binary.Base32;
import org.openqa.selenium.InvalidArgumentException;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DirectoryTotpManager {
    private final DirectoryManager directoryManager;
    private final List<String> totpUserIds;
    private DirectoryUserTotp currentGenerateUserTotpResponse;
    private String currentUserId;

    @Inject
    public DirectoryTotpManager(DirectoryManager directoryManager) {
        this.directoryManager = directoryManager;
        currentGenerateUserTotpResponse = null;
        totpUserIds = new ArrayList<String>();
    }

    public void generateUserTotp() throws Throwable {
        currentUserId = Utils.createRandomDirectoryUserName();
        currentGenerateUserTotpResponse = directoryManager.getDirectoryClient().generateUserTotp(currentUserId);
        totpUserIds.add(currentUserId);
    }

    public void removeTotpCodeForUser() throws Throwable {
        removeTotpCodeForUser(Utils.createRandomDirectoryUserName());
    }

    public void removeTotpCodeForUser(String userId) throws Throwable {
        directoryManager.getDirectoryClient().removeUserTotp(userId);
    }

    public DirectoryUserTotp getCurrentGenerateUserTotpResponse() {
        return currentGenerateUserTotpResponse;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    @After(order = 11000)
    public void cleanup() throws Throwable {
        while(totpUserIds.size() > 0){
            String userId = totpUserIds.get(0);
            removeTotpCodeForUser(userId);
            totpUserIds.remove(0);
        }
    }

    public String getCodeForCurrentUserTotpResponse() throws NoSuchAlgorithmException, InvalidKeyException {
        DirectoryUserTotp totpData = getCurrentGenerateUserTotpResponse();
        Duration timeStep = Duration.ofSeconds(totpData.getPeriod());
        int passwordLength = totpData.getDigits();
        String currentAlgorithm = totpData.getAlgorithm();
        String algorithm;
        switch(currentAlgorithm.toUpperCase())
        {
            case "SHA1":
                algorithm = TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA1;
                break;
            case "SHA256":
                algorithm = TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA256;
                break;
            case "SHA512":
                algorithm = TimeBasedOneTimePasswordGenerator.TOTP_ALGORITHM_HMAC_SHA512;
                break;
            default:
                throw new InvalidArgumentException("Unable to process algorithm: " + currentAlgorithm);
        }

        final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator(timeStep, passwordLength, algorithm);
        final Key key = new SecretKeySpec((new Base32()).decode(totpData.getSecret().getBytes(StandardCharsets.US_ASCII)), "RAW");
        final Instant now = Instant.now();
        return String.valueOf(totp.generateOneTimePassword(key, now));
    }
}
