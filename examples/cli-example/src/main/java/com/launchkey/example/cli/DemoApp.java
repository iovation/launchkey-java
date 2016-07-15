package com.launchkey.example.cli;

import com.launchkey.sdk.AppClient;
import com.launchkey.sdk.ClientFactory;
import com.launchkey.sdk.ClientFactoryBuilder;
import com.launchkey.sdk.domain.Device;
import com.launchkey.sdk.domain.WhiteLabelDeviceLinkData;
import com.launchkey.sdk.domain.auth.AuthResponse;
import com.launchkey.sdk.error.BaseException;
import com.launchkey.sdk.error.ExpiredAuthRequestException;
import com.launchkey.sdk.service.application.auth.AuthService;
import com.launchkey.sdk.service.organization.whitelabel.WhiteLabelService;
import com.martiansoftware.jsap.*;
import org.apache.http.client.HttpClient;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Copyright 2016 LaunchKey, Inc. All rights reserved.
 * <p>
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
            ClientFactory clientFactory = getClientFactory(
                    commandLine.getURL("base-url").toString(),
                    !commandLine.getBoolean("no-verify")
            );

            if (command.equalsIgnoreCase("application")) {
                JSAP appJsap = getApplicationJSAP();
                if (help) {
                    printHelp(appJsap);
                } else {
                    JSAPResult result = appJsap.parse(commandArgs);
                    processAppCommand(clientFactory, result);
                }
            } else if (command.equalsIgnoreCase("white-label")) {
                JSAP orgJsap = getWhiteLabelJSAP();
                if (help) {
                    printHelp(orgJsap);
                } else {
                    JSAPResult result = orgJsap.parse(commandArgs);
                    processWhiteLabelCommand(clientFactory, result);
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

    private static void processWhiteLabelCommand(ClientFactory clientFactory, JSAPResult commandData) throws IOException, BaseException, JSAPException {
        Long orgKey = commandData.getLong("org-key", 0L);
        String privateKeyLocation = commandData.getString("private-key-file");
        String sdkKey = commandData.getString("sdk-key");
        String action = commandData.getString("action");
        String[] actionOptions = commandData.getStringArray("action-args");
        if (action != null && action.equalsIgnoreCase("device-link")) {
            if (actionOptions.length == 1) {
                handleWhiteLabelDeviceLink(actionOptions[0], getWhiteLabelService(clientFactory, orgKey, sdkKey, privateKeyLocation));
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
                handleWhiteLabelDevicesList(actionOptions[0], getWhiteLabelService(clientFactory, orgKey, sdkKey, privateKeyLocation));
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
                handleWhiteLabelDeviceUnlink(actionOptions[0], actionOptions[1], getWhiteLabelService(clientFactory, orgKey, sdkKey, privateKeyLocation));
            } else {
                System.out.println();
                System.out.println(
                        "device-unlink action requires two action arguments which are the unique ID of the " +
                                "user for your application and the name of the device to delete"
                );
                System.out.println();
            }
        } else {
            if (action == null) {
                System.out.println();
                System.out.println("Error: Unknown action");
            }
            printHelp(getWhiteLabelJSAP());
        }
    }

    private static WhiteLabelService getWhiteLabelService(
            ClientFactory clientFactory, Long orgKey, String sdkKey, String privateKeyLocation
    ) throws IOException {
        return clientFactory.makeOrgClient(orgKey, readFile(privateKeyLocation))
                    .whiteLabel(sdkKey);
    }

    private static void processAppCommand(ClientFactory clientFactory, JSAPResult commandData) throws JSAPException, IOException, BaseException, InterruptedException {
        Long appKey = commandData.getLong("app-key", 0L);
        String secretKey = commandData.getString("secret-key");
        String privateKeyLocation = commandData.getString("private-key-file");
        String action = commandData.getString("action");
        String[] actionOptions = commandData.getStringArray("action-args");
        if (action != null && action.equalsIgnoreCase("login")) {
            if (actionOptions.length == 1) {
                handleLogin(
                        actionOptions[0],
                        null,
                        getAppClient(clientFactory, appKey, secretKey, privateKeyLocation).auth()
                );
            } else if (actionOptions.length == 2) {
                handleLogin(
                        actionOptions[0],
                        actionOptions[1],
                        getAppClient(clientFactory, appKey, secretKey, privateKeyLocation).auth()
                );
            } else {
                System.out.println();
                System.out.println(
                        "The login action requires a single action argument which is the username and can " +
                                "optionally take a second action argument which is the \"context\" with which " +
                                "to perform a login"
                );
                System.out.println();
            }
        } else if (action != null && action.equalsIgnoreCase("authorized")) {
            if (actionOptions.length == 1) {
                Boolean authorized;
                try {
                    AuthResponse authResponse = getAppClient(
                            clientFactory,
                            appKey,
                            secretKey,
                            privateKeyLocation
                    ).auth().getAuthResponse(actionOptions[0]);
                    authorized = (authResponse == null) ? false : authResponse.isAuthorized();
                } catch (ExpiredAuthRequestException e) { // If the auth has an expired status, it has been logged out
                    authorized = false;
                }
                System.out.println();
                System.out.println("User is " + (authorized ? "still" : "not") + " authorized");
                System.out.println();
            } else {
                System.out.println();
                System.out.println(
                        "The authorized action takes a single action argument which is the Auth Request value " +
                                "returned from a login action"
                );
                System.out.println();
            }
        } else if (action != null && action.equalsIgnoreCase("logout")) {
            if (actionOptions.length == 1) {
                getAppClient(clientFactory, appKey, secretKey, privateKeyLocation).auth().logout(actionOptions[0]);
                System.out.println();
                System.out.println("User is logged out.");
                System.out.println();
            } else {
                System.out.println();
                System.out.println(
                        "The authorized action takes a single action argument which is the Auth Request value "
                                + "returned from a login action"
                );
                System.out.println();
            }

        } else {
            if (action == null) {
                System.out.println();
                System.out.println("Error: Unknown action");
            }
            printHelp(getApplicationJSAP());
        }
    }

    private static AppClient getAppClient(
            ClientFactory clientFactory, Long appKey, String secretKey, String privateKeyLocation
    ) throws IOException {
        return clientFactory.makeAppClient(appKey, secretKey, readFile(privateKeyLocation));
    }

    private static ClientFactory getClientFactory(
            String baseUrl, boolean verifySSL
    ) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        ClientFactoryBuilder builder = new ClientFactoryBuilder()
                .setJCEProvider(new BouncyCastleProvider())
                .setAPIBaseURL(baseUrl);

        if (!verifySSL) {
            HttpClient httpClient = getHttpClientWithoutSslVerify();
            builder.setHttpClient(httpClient);
        }

        return builder.build();
    }

    private static HttpClient getHttpClientWithoutSslVerify() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        SSLContextBuilder builder = SSLContexts.custom();
        builder.loadTrustMaterial(null, new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                return true;
            }
        });
        SSLContext sslContext = builder.build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext, new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl)
                    throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert)
                    throws SSLException {
            }

            @Override
            public void verify(
                    String host, String[] cns,
                    String[] subjectAlts
            ) throws SSLException {
            }

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("https", sslsf)
                .build();

        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, null, null, null, 30, TimeUnit.SECONDS
        );

        connectionManager.setMaxTotal(30);
        connectionManager.setDefaultMaxPerRoute(30);
        return HttpClients
                .custom()
                .setConnectionManager(connectionManager)
                .build();
    }

    private static void handleWhiteLabelDeviceUnlink(
            String identifier, String deviceName, WhiteLabelService whiteLabelService
    ) throws BaseException {
        System.out.println();
        whiteLabelService.unlinkDevice(identifier, deviceName);
        System.out.println("Device unlinked");
        System.out.println();
    }

    private static void handleWhiteLabelDevicesList(
            String identifier, WhiteLabelService whiteLabelService
    ) throws BaseException {
        List<Device> devices = whiteLabelService.getLinkedDevices(identifier);
        System.out.println();
        System.out.println("Devices:");
        for (Device device : devices) {
            System.out.println("  " + device.getName() + ":");
            System.out.println("    Type:    " + device.getType());
            System.out.println("    Status:  " + device.getStatus());
            System.out.println("    Created: " + device.getCreated());
            System.out.println("    Updated: " + device.getUpdated());
        }
        System.out.println();


    }

    private static void handleWhiteLabelDeviceLink(
            String identifier, WhiteLabelService whiteLabelService
    ) throws BaseException {
        WhiteLabelDeviceLinkData result = whiteLabelService.linkDevice(identifier);
        System.out.println();
        System.out.println("Device link request successful");
        System.out.println("    QR Code URL: " + result.getQrCodeUrl());
        System.out.println("    Manual verification code: " + result.getCode());
        System.out.println();
    }

    private static void handleLogin(
            String username, String context, AuthService authService
    ) throws BaseException, InterruptedException {
        String authRequest = authService.login(username, context);
        System.out.println();
        System.out.println("Login request successful");
        System.out.println("    Auth Request: " + authRequest);
        System.out.print("Checking for response from user");
        System.out.println();
        AuthResponse authResponse;
        long started = new Date().getTime();
        while (new Date().getTime() - started < 30000) {
            Thread.sleep(1000L);
            System.out.print(".");
            authResponse = authService.getAuthResponse(authRequest);
            if (authResponse != null) {
                System.out.println();
                System.out.println("Login request " + (authResponse.isAuthorized() ? "accepted" : "denied") + " by user");
                System.out.println("    Auth Request: " + authResponse.getAuthRequestId());
                System.out.println("    Device ID:    " + authResponse.getDeviceId());
                System.out.println("    user Hash:    " + authResponse.getUserHash());
                System.out.println("    User Push ID: " + authResponse.getUserPushId());
                String orgUserId = authResponse.getOrganizationUserId() == null ? "N/A" : authResponse.getOrganizationUserId();
                System.out.println("    Org User ID:  " + orgUserId);
                System.out.println();
                return;
            }
        }
        System.out.println();
        System.out.println("Login request timed out");
        System.out.println();
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
                .setHelp("Base URL to the Platform API");
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
                        .setHelp("Command to execute.  [application, white-label]")
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

    private static JSAP getApplicationJSAP() throws JSAPException {
        JSAP jsap = new JSAP();
        jsap.registerParameter(
                new UnflaggedOption("app-key")
                        .setUsageName("APP_KEY")
                        .setRequired(true)
                        .setStringParser(JSAP.LONG_PARSER)
                        .setHelp("Application Key for the Application. It is found in the Keys section of " +
                                "the Application's page in Dashboard.")
        );
        jsap.registerParameter(
                new UnflaggedOption("secret-key")
                        .setUsageName("SECRET_KEY")
                        .setRequired(true)
                        .setHelp("Secret Key for the application. It is found in the Keys section of " +
                                "the Application's page in Dashboard.")
        );
        jsap.registerParameter(
                new UnflaggedOption("private-key-file")
                        .setUsageName("PRIVATE_KEY_FILE")
                        .setRequired(true)
                        .setHelp("File location of the RSA Private Key of the RSA public/private " +
                                "key pair whose public key is associated with the Application.")
        );
        jsap.registerParameter(
                new UnflaggedOption("action")
                        .setUsageName("ACTION")
                        .setRequired(true)
                        .setHelp("Action to execute.  [login, authorized, logout]")
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

    private static JSAP getWhiteLabelJSAP() throws JSAPException {
        JSAP jsap = new JSAP();
        jsap.registerParameter(
                new UnflaggedOption("org-key")
                        .setUsageName("ORG_KEY")
                        .setStringParser(JSAP.LONG_PARSER)
                        .setRequired(true)
                        .setHelp("Organization Key for the Organization. It is found in the Keys section of " +
                                "the Organization's page in Dashboard.")
        );
        jsap.registerParameter(
                new UnflaggedOption("sdk-key")
                        .setUsageName("SDK_KEY")
                        .setRequired(true)
                        .setHelp("SDK Key for the White Label Group. It is found in the General section of the " +
                                "White Label Groups's page in Dashboard.")
        );
        jsap.registerParameter(
                new UnflaggedOption("private-key-file")
                        .setUsageName("PRIVATE_KEY_FILE")
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

    @SuppressWarnings("ThrowFromFinallyBlock")
    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        try {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }
}
