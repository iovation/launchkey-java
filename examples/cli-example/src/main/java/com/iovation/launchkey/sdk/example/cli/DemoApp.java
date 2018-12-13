package com.iovation.launchkey.sdk.example.cli;

import com.iovation.launchkey.sdk.FactoryFactory;
import com.iovation.launchkey.sdk.FactoryFactoryBuilder;
import com.iovation.launchkey.sdk.client.DirectoryClient;
import com.iovation.launchkey.sdk.client.ServiceClient;
import com.iovation.launchkey.sdk.client.ServiceFactory;
import com.iovation.launchkey.sdk.domain.directory.Device;
import com.iovation.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import com.iovation.launchkey.sdk.domain.service.AuthorizationRequest;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import com.iovation.launchkey.sdk.error.BaseException;
import com.martiansoftware.jsap.*;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DemoApp {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        try {
            JSAP jsap = getRootJSAP();

            JSAPResult commandLine = jsap.parse(args);
            String command = commandLine.getString("command");

            if (command == null || command.isEmpty()) {
                printHelp(jsap);
                return;
            }

            boolean help = commandLine.getBoolean("help");
            String[] commandArgs = commandLine.getStringArray("command-args");
            Provider provider = new BouncyCastleProvider();
            FactoryFactory factoryFactory = getFactoryFactory(
                    commandLine.getURL("base-url").toString(),
                    !commandLine.getBoolean("no-verify"),
                    provider);

            if (command.equalsIgnoreCase("service")) {
                JSAP serviceJsap = getServiceJSAP();
                if (help) {
                    printHelp(serviceJsap);
                } else {
                    JSAPResult result = serviceJsap.parse(commandArgs);
                    processServiceCommand(factoryFactory, result);
                }
            } else if (command.equalsIgnoreCase("directory")) {
                JSAP orgJsap = getDirectoryJSAP();
                if (help) {
                    printHelp(orgJsap);
                } else {
                    JSAPResult result = orgJsap.parse(commandArgs);
                    processDirectoryCommand(factoryFactory, result);
                }
            } else {
                System.out.println();
                System.out.println("Error: Unknown command");
                printHelp(jsap);
            }

        } catch (IOException e) {
            System.out.println();
            System.out.println("There was an error reading the private key file");
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println();
            System.out.println("The command was interrupted: " + e.getMessage());
            System.out.println();
        } catch (JSAPException e) {
            System.out.println();
            System.out.println("Unexpected parse exception: " + e.getMessage());
            System.out.println();
        } catch (BaseException e) {
            System.out.println();
            System.out.println("There was an error executing your command: " + e.getMessage() + ": " + e.getErrorCode());
            if (e.getCause() != null) {
                System.out.println("  Cause by: " + e.getCause().getMessage());
            }
            System.out.println();
        }
    }

    private static void processDirectoryCommand(FactoryFactory factoryFactory, JSAPResult commandData) throws IOException, BaseException, JSAPException {
        String directoryId = commandData.getString("dir-id");
        String privateKeyLocation = commandData.getString("key-file");
        String action = commandData.getString("action");
        String[] actionOptions = commandData.getStringArray("action-args");
        if (action != null && action.equalsIgnoreCase("device-link")) {
            if (actionOptions.length == 1) {
                handleDirectoryUserDeviceLink(actionOptions[0], getDirectoryFactory(factoryFactory, directoryId, privateKeyLocation));
            } else {
                System.out.println();
                System.out.println(
                        "device-link action requires a single action argument which is the unique ID " +
                                "of the user for your application"
                );
                System.out.println();
            }
        } else if (action != null && action.equalsIgnoreCase("devices-list")) {
            if (actionOptions.length == 1) {
                handleDirectoryUserDevicesList(actionOptions[0], getDirectoryFactory(factoryFactory, directoryId, privateKeyLocation));
            } else {
                System.out.println();
                System.out.println(
                        "devices-list action requires a single action argument which is the unique ID of the user " +
                                "for your application"
                );
                System.out.println();
            }
        } else if (action != null && action.equalsIgnoreCase("device-unlink")) {
            if (actionOptions.length == 2) {
                handleDirectoryUserDeviceUnlink(actionOptions[0], actionOptions[1], getDirectoryFactory(factoryFactory, directoryId, privateKeyLocation));
            } else {
                System.out.println();
                System.out.println(
                        "device-unlink action requires two action arguments which are the unique ID of the " +
                                "user for your application and the ID of the device to delete"
                );
                System.out.println();
            }
        } else {
            if (action == null) {
                System.out.println();
                System.out.println("Error: Unknown action");
            }
            printHelp(getDirectoryJSAP());
        }
    }

    private static DirectoryClient getDirectoryFactory(
            FactoryFactory factoryFactory, String directoryId, String privateKeyLocation
    ) throws IOException {
        return factoryFactory.makeDirectoryFactory(directoryId, readFile(privateKeyLocation))
                .makeDirectoryClient();
    }

    private static void processServiceCommand(FactoryFactory factoryFactory, JSAPResult commandData) throws JSAPException, IOException, BaseException, InterruptedException {
        String serviceId = commandData.getString("svc-id");
        String privateKeyLocation = commandData.getString("key-file");
        String action = commandData.getString("action");
        String[] actionOptions = commandData.getStringArray("action-args");
        if (action != null && action.equalsIgnoreCase("authorize")) {
            if (actionOptions.length == 1) {
                handleAuthorize(
                        actionOptions[0],
                        null,
                        getServiceFactory(factoryFactory, serviceId, privateKeyLocation).makeServiceClient()
                );
            } else if (actionOptions.length == 2) {
                handleAuthorize(
                        actionOptions[0],
                        actionOptions[1],
                        getServiceFactory(factoryFactory, serviceId, privateKeyLocation).makeServiceClient()
                );
            } else {
                System.out.println();
                System.out.println(
                        "The authorize action requires a single action argument which is the End User's username and can " +
                                "optionally take a second action argument which is the \"context\" with which " +
                                "to perform the request"
                );
                System.out.println();
            }
        } else if (action != null && action.equalsIgnoreCase("session-start")) {
            if (actionOptions.length == 1) {
                getServiceFactory(factoryFactory, serviceId, privateKeyLocation).makeServiceClient().sessionStart(actionOptions[0]);
                System.out.println();
                System.out.println("User session is started.");
                System.out.println();
            } else {
                System.out.println();
                System.out.println(
                        "The session-start action takes a single action argument which is the End User's username"
                );
                System.out.println();
            }

        } else if (action != null && action.equalsIgnoreCase("session-end")) {
            if (actionOptions.length == 1) {
                getServiceFactory(factoryFactory, serviceId, privateKeyLocation).makeServiceClient().sessionEnd(actionOptions[0]);
                System.out.println();
                System.out.println("User session is ended.");
                System.out.println();
            } else {
                System.out.println();
                System.out.println(
                        "The session-end action takes a single action argument which is the End User's username"
                );
                System.out.println();
            }

        } else {
            if (action == null) {
                System.out.println();
                System.out.println("Error: Unknown action");
            }
            printHelp(getServiceJSAP());
        }
    }

    private static ServiceFactory getServiceFactory(
            FactoryFactory factoryFactory, String serviceId, String privateKeyLocation
    ) throws IOException {
        return factoryFactory.makeServiceFactory(serviceId, readFile(privateKeyLocation));
    }

    private static FactoryFactory getFactoryFactory(
            String baseUrl, boolean verifySSL,
            Provider provider) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        FactoryFactoryBuilder builder = new FactoryFactoryBuilder()
                .setJCEProvider(provider)
                .setAPIBaseURL(baseUrl);

        if (!verifySSL) {
            HttpClient httpClient = getHttpClientWithoutSslVerify();
            builder.setHttpClient(httpClient);
        }

        return builder.build();
    }

    private static HttpClient getHttpClientWithoutSslVerify() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        final SSLContextBuilder builder = SSLContexts.custom();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                return true;
            }
        });
        final SSLContext sslContext = builder.build();
        final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
                builder.build());

        final Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("https", socketFactory)
                .build();

        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, null, null, null, 30, TimeUnit.SECONDS
        );

        connectionManager.setMaxTotal(30);
        connectionManager.setDefaultMaxPerRoute(30);
        return HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    private static void handleDirectoryUserDeviceUnlink(
            String identifier, String deviceName, DirectoryClient directoryClient
    ) throws BaseException {
        System.out.println();
        directoryClient.unlinkDevice(identifier, deviceName);
        System.out.println("Device unlinked");
        System.out.println();
    }

    private static void handleDirectoryUserDevicesList(
            String identifier, DirectoryClient directoryClient
    ) throws BaseException {
        List<Device> devices = directoryClient.getLinkedDevices(identifier);
        System.out.println();
        System.out.println("Devices:");
        for (Device device : devices) {
            System.out.println("  " + device.getId() + ":");
            System.out.println("    Name:    " + device.getName());
            System.out.println("    Type:    " + device.getType());
            System.out.println("    Status:  " + device.getStatus());
            System.out.println("    Created: " + device.getCreated());
            System.out.println("    Updated: " + device.getUpdated());
        }
        System.out.println();
    }

    private static void handleDirectoryUserDeviceLink(
            String identifier, DirectoryClient directoryClient
    ) throws BaseException {
        DirectoryUserDeviceLinkData result = directoryClient.linkDevice(identifier);
        System.out.println();
        System.out.println("Device link request successful");
        System.out.println("    QR Code URL: " + result.getQrCodeUrl());
        System.out.println("    Manual verification code: " + result.getCode());
        System.out.println();
    }

    private static void handleAuthorize(
            String username, String context, ServiceClient serviceClient
    ) throws BaseException, InterruptedException {
        AuthorizationRequest authRequest = serviceClient.createAuthorizationRequest(username, context);
        System.out.println();
        System.out.println("Authorization request successful");
        System.out.println("    Auth Request: " + authRequest);
        System.out.print("Checking for response from the End User");
        System.out.println();
        AuthorizationResponse authorizationResponse;
        long started = new Date().getTime();
        while (new Date().getTime() - started < 30000) {
            Thread.sleep(1000L);
            System.out.print(".");
            authorizationResponse = serviceClient.getAuthorizationResponse(authRequest.getId());
            if (authorizationResponse != null) {
                System.out.println();
                System.out.println("Authorization request " + (authorizationResponse.isAuthorized() ? "accepted" : "denied") + " by user");
                System.out.println("    Type:          " + naForNull(authorizationResponse.getType()));
                System.out.println("    Reason:        " + naForNull(authorizationResponse.getReason()));
                System.out.println("    Denial Reason: " + naForNull(authorizationResponse.getDenialReason()));
                System.out.println("    Fraud:         " + naForNull(authorizationResponse.isFraud()));
                System.out.println("    Device ID:     " + authorizationResponse.getDeviceId());
                System.out.println("    Svc User Hash: " + authorizationResponse.getServiceUserHash());
                System.out.println("    User Push ID:  " + authorizationResponse.getUserPushId());
                System.out.println("    Org User Hash: " + naForNull(authorizationResponse.getOrganizationUserHash()));
                System.out.println();
                return;
            }
        }
        System.out.println();
        System.out.println("Authorization request timed out");
        System.out.println();
    }

    private static String naForNull(String value) {
        return value == null ? "N/A" : value;
    }

    private static String naForNull(Enum value) {
        return value == null ? "N/A" : value.name();
    }

    private static String naForNull(Boolean value) {
        return value == null ? "N/A" : value.toString();
    }

    private static void printHelp(JSAP jsap) {
        String jar = new File(DemoApp.class.getProtectionDomain().getCodeSource().getLocation().getPath())
                .getName();
        System.out.println();
        System.out.println();
        System.out.println("java -jar " + jar + " " + jsap.getUsage());
        System.out.println();
        System.out.println(jsap.getHelp());
        System.out.println();
        System.out.println();
    }

    private static JSAP getRootJSAP() throws JSAPException {
        JSAP jsap = new JSAP();
        jsap.registerParameter(
                new Switch("help")
                        .setShortFlag('h')
                        .setLongFlag("help")
                        .setHelp("Get help on using this application")
        );
        Parameter baseUrl = new FlaggedOption("base-url")
                .setStringParser(JSAP.URL_PARSER)
                .setShortFlag('u')
                .setLongFlag("base-url")
                .setHelp("Base URL to the iovation LaunchKey API");
        baseUrl.addDefault("https://api.launchkey.com");
        jsap.registerParameter(
                baseUrl
        );
        jsap.registerParameter(
                new Switch("no-verify")
                        .setShortFlag('n')
                        .setLongFlag("no-verify")
                        .setHelp("Disable SSL certificate verification. Required for getting past certificate " +
                                "verification errors for self-signed certs or local CA's")
        );
        jsap.registerParameter(
                new UnflaggedOption("command")
                        .setUsageName("COMMAND")
                        .setRequired(true)
                        .setHelp("Command to execute.  [service, directory]")
        );
        jsap.registerParameter(
                new UnflaggedOption("command-args")
                        .setUsageName("COMMAND_ARG")
                        .setList(true)
                        .setGreedy(true)
                        .setListSeparator(' ')
                        .setHelp("Arguments to pass to the command. Specifying this argument will give you help " +
                                "for the specified command.")
        );
        return jsap;
    }

    private static JSAP getServiceJSAP() throws JSAPException {
        JSAP jsap = new JSAP();
        jsap.registerParameter(
                new UnflaggedOption("svc-id")
                        .setUsageName("SVC_ID")
                        .setRequired(true)
                        .setHelp("Service ID. It is found in the Keys section of " +
                                "the Service's profile page in Dashboard.")
        );
        jsap.registerParameter(
                new UnflaggedOption("key-file")
                        .setUsageName("KEY_FILE")
                        .setRequired(true)
                        .setHelp("File location of the RSA Private Key of the RSA public/private " +
                                "key pair whose public key is associated with the Service.")
        );
        jsap.registerParameter(
                new UnflaggedOption("action")
                        .setUsageName("ACTION")
                        .setRequired(true)
                        .setHelp("Action to execute.  [authorize, session-start, session-end]")
        );
        jsap.registerParameter(
                new UnflaggedOption("action-args")
                        .setUsageName("ACTION_ARG")
                        .setList(true)
                        .setGreedy(true)
                        .setListSeparator(' ')
                        .setHelp("Options for the action")
        );
        return jsap;
    }

    private static JSAP getDirectoryJSAP() throws JSAPException {
        JSAP jsap = new JSAP();
        jsap.registerParameter(
                new UnflaggedOption("dir-id")
                        .setUsageName("DIR_ID")
                        .setRequired(true)
                        .setHelp("Directory ID. It is found in the Keys section of " +
                                "the Directory's page in Dashboard.")
        );
        jsap.registerParameter(
                new UnflaggedOption("key-file")
                        .setUsageName("KEY_FILE")
                        .setRequired(true)
                        .setHelp("File location of the RSA Private Key of the RSA public/private key pair " +
                                "whose public key is associated with the Organization.")
        );
        jsap.registerParameter(
                new UnflaggedOption("action")
                        .setUsageName("ACTION")
                        .setRequired(true)
                        .setHelp("Action to execute.  [device-link, devices-list, device-unlink]")
        );
        jsap.registerParameter(
                new UnflaggedOption("action-args")
                        .setUsageName("ACTION_ARG")
                        .setList(true)
                        .setGreedy(true)
                        .setListSeparator(' ')
                        .setHelp("Options for the action")
        );
        return jsap;
    }

    @SuppressWarnings({"ThrowFromFinallyBlock", "Duplicates"})
    private static String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        }
        return sb.toString();
    }
}
