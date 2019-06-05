package com.iovation.launchkey.sdk.integration.managers.kobiton;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.iovation.launchkey.sdk.error.InvalidRequestException;
import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.GenericHeader;
import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.GenericRequest;
import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.GenericResponse;
import com.iovation.launchkey.sdk.integration.managers.kobiton.transport.RequestFactory;
import org.jose4j.base64url.Base64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KobitonManager {

    public class KobitonBundle {
        public final String appId;
        public final String appLocation;

        public KobitonBundle(int appId) {
            this.appId = String.valueOf(appId);
            this.appLocation = "kobiton-store:" + this.appId;
        }
    }

    private final RequestFactory requestFactory;
    private final String kobitonUploadUrl;
    private final String kobitonAuthCreds;
    private final String kobitonAppsUrl;
    private final String kobitonDevicesUrl;
    public KobitonManager(RequestFactory requestFactory,
                          String kobitonUploadUrl,
                          String kobitonAuthCreds,
                          String kobitonAppsUrl,
                          String kobitonDevicesUrl) {
        this.requestFactory = requestFactory;
        this.kobitonUploadUrl = kobitonUploadUrl;
        this.kobitonAuthCreds = kobitonAuthCreds;
        this.kobitonAppsUrl = kobitonAppsUrl;
        this.kobitonDevicesUrl = kobitonDevicesUrl;
    }

    private void throwErrorIfStatusCodeIsBad(int statusCode) {
        if (statusCode != 200) {
            throw new IllegalStateException("Response from Kobiton is bad");
        }
    }

    private GenericResponse generateUploadUrl(String appName) {
        // Generate upload URL
        GenericResponse response = requestFactory.getDefaultRequestBuilder()
                .setRequestType(GenericRequest.RequestType.POST)
                .setURL(kobitonUploadUrl)
                .addHeader(new GenericHeader("Authorization", "Basic ".concat(Base64.encode(kobitonAuthCreds.getBytes()))))
                .addContentTypeJSONHeader()
                .addHeader(new GenericHeader("Accept", GenericRequest.APPLICATION_JSON))
                .setBody(String.format("{\"filename\": \"%s\"}", appName))
                .build()
                .send();

        throwErrorIfStatusCodeIsBad(response.getStatusCode());

        return response;
    }

    private GenericResponse uploadFileToS3(String s3UploadUrl, String appPhysicalLocation) {
        // Generate upload URL
        GenericResponse response = requestFactory.getDefaultRequestBuilder()
                .setRequestType(GenericRequest.RequestType.PUT)
                .setURL(s3UploadUrl)
                .addHeader(new GenericHeader(GenericRequest.CONTENT_TYPE, GenericRequest.APPLICATION_OCTET_STREAM))
                .addHeader(new GenericHeader("x-amz-tagging", "unsaved=true"))
                .setBodyToFile(appPhysicalLocation)
                .build()
                .send();

        throwErrorIfStatusCodeIsBad(response.getStatusCode());

        return response;
    }

    private GenericResponse createApplicationOnKobiton(String appName, String appPath) {
        GenericResponse response = requestFactory.getDefaultRequestBuilder()
                .setRequestType(GenericRequest.RequestType.POST)
                .setURL(kobitonAppsUrl)
                .addHeader(new GenericHeader("Authorization", "Basic ".concat(Base64.encode(kobitonAuthCreds.getBytes()))))
                .addContentTypeJSONHeader()
                .setBody(String.format("{\"filename\": \"%s\", \"appPath\": \"%s\"}", appName, appPath))
                .build()
                .send();

        throwErrorIfStatusCodeIsBad(response.getStatusCode());

        return response;
    }

    public KobitonBundle createApplication(String appName, String appLocation) {

        // Generate upload URL
        GenericResponse response = generateUploadUrl(appName);

        JsonObject object = new JsonParser().parse(response.getBody()).getAsJsonObject();
        String appPath = object.get("appPath").getAsString();
        String s3UploadUrl = object.get("url").getAsString();

        // Upload file to S3
        response = uploadFileToS3(s3UploadUrl, appLocation);

        // Create the app on Kobiton
        response = createApplicationOnKobiton(appName, appPath);

        JsonObject object2 = new JsonParser().parse(response.getBody()).getAsJsonObject();
        int appId = object2.get("appId").getAsInt();

        KobitonBundle bundle = new KobitonBundle(appId);

        // Kobiton's appium server sometimes takes a second to realize the app is uploaded
        System.out.println("App finished uploading, giving Kobiton some time to process the uploaded app...");
        try {
            Thread.sleep(3000);
        } catch(InterruptedException e) {
            System.out.println("Error sleeping this thread, continuing anyways");
        }
        System.out.println("...Time's up");

        return bundle;
    }

    public void deleteApplication(String appId) {

        // Delete the app on Kobiton
        GenericResponse response = requestFactory.getDefaultRequestBuilder()
                .setRequestType(GenericRequest.RequestType.DELETE)
                .setURL(kobitonAppsUrl + appId)
                .addHeader(new GenericHeader("Authorization", "Basic ".concat(Base64.encode(kobitonAuthCreds.getBytes()))))
                .addContentTypeJSONHeader()
                .build()
                .send();

        throwErrorIfStatusCodeIsBad(response.getStatusCode());
    }

    public List<KobitonDevice> getAllDevices() throws InvalidRequestException {

        // Get all devices on Kobiton
        GenericResponse response = requestFactory.getDefaultRequestBuilder()
                .setRequestType(GenericRequest.RequestType.GET)
                .setURL(kobitonDevicesUrl)
                .addHeader(new GenericHeader("Authorization", "Basic ".concat(Base64.encode(kobitonAuthCreds.getBytes()))))
                .addHeader(new GenericHeader("Accept", GenericRequest.APPLICATION_JSON))
                .build()
                .send();

        throwErrorIfStatusCodeIsBad(response.getStatusCode());

        List<KobitonDevice> devices = new ArrayList<>();

        JsonObject responseAsJson = new JsonParser().parse(response.getBody()).getAsJsonObject();

        JsonArray privateDevices = responseAsJson.getAsJsonArray("privateDevices");
        for (JsonElement element : privateDevices) {
            try {
                devices.add(new ObjectMapper().readValue(element.toString(), KobitonPrivateDevice.class));
            } catch (JsonParseException e) {
                throw new InvalidRequestException("Unable to parse the decrypted body as JSON!", e, null);
            } catch (IOException e) {
                throw new InvalidRequestException("Unable to read the body due to an I/O error!", e, null);
            }
        }

        JsonArray cloudDevices = responseAsJson.getAsJsonArray("cloudDevices");
        for (JsonElement element : cloudDevices) {
            try {
                devices.add(new ObjectMapper().readValue(element.toString(), KobitonCloudDevice.class));
            } catch (JsonParseException e) {
                throw new InvalidRequestException("Unable to parse the decrypted body as JSON!", e, null);
            } catch (IOException e) {
                throw new InvalidRequestException("Unable to read the body due to an I/O error!", e, null);
            }
        }

        return devices;
    }

    public void makeApplicationPublic(String appId) {

        // Make app public on kobiton
        GenericResponse response = requestFactory.getDefaultRequestBuilder()
                .setRequestType(GenericRequest.RequestType.PUT)
                .setURL(kobitonAppsUrl + appId + "/public")
                .addHeader(new GenericHeader("Authorization", "Basic ".concat(Base64.encode(kobitonAuthCreds.getBytes()))))
                .build()
                .send();

        throwErrorIfStatusCodeIsBad(response.getStatusCode());
    }

    public void makeApplicationPrivate(String appId) {

        // Make app private on kobiton
        GenericResponse response = requestFactory.getDefaultRequestBuilder()
                .setRequestType(GenericRequest.RequestType.PUT)
                .setURL(kobitonAppsUrl + appId + "/private")
                .addHeader(new GenericHeader("Authorization", "Basic ".concat(Base64.encode(kobitonAuthCreds.getBytes()))))
                .build()
                .send();

        throwErrorIfStatusCodeIsBad(response.getStatusCode());
    }
}
