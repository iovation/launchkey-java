package com.launchkey.example.cli;

import com.launchkey.sdk.BasicClient;
import com.launchkey.sdk.Client;
import com.launchkey.sdk.service.auth.AuthResponse;
import com.launchkey.sdk.service.auth.AuthService;
import com.launchkey.sdk.service.error.ApiException;
import com.launchkey.sdk.service.error.ExpiredAuthRequestException;
import com.launchkey.sdk.service.whitelabel.LinkResponse;
import com.launchkey.sdk.service.whitelabel.WhiteLabelService;
import com.martiansoftware.jsap.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

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

    public static void main(String[] args) {

        String privateKeyFile = null;
        try {
            JSAP jsap = getJSAP();

            JSAPResult commandLine = jsap.parse(args);
            String appKey = commandLine.getString("app_key");
            String secretKey = commandLine.getString("secret_key");
            privateKeyFile = commandLine.getString("private_key_file");

            if (commandLine.getBoolean("help") || appKey == null || secretKey == null || privateKeyFile == null) {
                printHelp(jsap);
                return;
            }

            Client client = BasicClient.factory(
                    Long.parseLong(appKey),
                    secretKey,
                    readFile(privateKeyFile),
                    new BouncyCastleProvider()
            );

            String command = commandLine.getString("command");
            String[] commandOptions = commandLine.getStringArray("command-options");
            if (command.equalsIgnoreCase("login")) {
                if (commandOptions.length == 1) {
                    handleLogin(commandOptions[0], null, client.auth());
                } else if (commandOptions.length == 2) {
                    handleLogin(commandOptions[0], commandOptions[1], client.auth());
                } else {
                    System.out.println();
                    System.out.println(
                            "The login command requires a single command option which is the username and can " +
                                    "optionally take a second command argument which is the \"context\" with which " +
                                    "to perform a login"
                    );
                    printHelp(jsap);
                    System.out.println();
                }
            } else if (command.equalsIgnoreCase("authorized")) {
                if (commandOptions.length == 1) {
                    Boolean authorized;
                    try {
                        AuthResponse authResponse = client.auth().getAuthResponse(commandOptions[0]);
                        authorized = authResponse.isAuthorized();
                    } catch (ExpiredAuthRequestException e) { // If the auth has an expired status, it has been logged out
                        authorized = false;
                    }
                    System.out.println();
                    System.out.println("User is " + (authorized ? "still" : "not") + " authorized");
                    System.out.println();
                } else {
                    System.out.println();
                    System.out.println(
                            "The authorized command takes a single command option which is the Auth Request value " +
                                    "returned from a login command"
                    );
                    printHelp(jsap);
                    System.out.println();
                }
            } else if (command.equalsIgnoreCase("logout")) {
                if (commandOptions.length == 1) {
                    client.auth().logout(commandOptions[0]);
                    System.out.println();
                    System.out.println("User is logged out.");
                    System.out.println();
                } else {
                    System.out.println();
                    System.out.println(
                            "The authorized command takes a single command option which is the Auth Request value "
                                    + "returned from a login command"
                    );
                    printHelp(jsap);
                    System.out.println();
                }
            } else if (command.equalsIgnoreCase("white-label-user-link")) {
                if (commandOptions.length == 1) {
                    handleWhiteLabelLinkUser(commandOptions[0], client.whiteLabel());
                } else {
                    System.out.println();
                    System.out.println(
                            "white-label-user-link command requires a single argument of the unique ID for your " +
                                    "application"
                    );
                    printHelp(jsap);
                    System.out.println();
                }
            } else {
                System.out.println("Unknown command \"" + command + "\"");
                printHelp(jsap);
            }

        } catch (IOException ioe) {
            System.out.println();
            System.out.println("There was an error reading the private key file: " + privateKeyFile);
            System.out.println();
        } catch (ApiException e) {
            System.out.println();
            System.out.println("There was an error executing your command: " + e.getMessage());
            System.out.println();
        } catch (InterruptedException e) {
            System.out.println();
            System.out.println("The command was interrupted: " + e.getMessage());
            System.out.println();
        } catch (JSAPException e) {
            System.out.println();
            System.out.println("Unexpected parse exception: " + e.getMessage());
            System.out.println();
        }
    }

    private static void handleWhiteLabelLinkUser(
            String identifier, WhiteLabelService whiteLabelService
    ) throws ApiException {
        LinkResponse result = whiteLabelService.linkUser(identifier);
        System.out.println();
        System.out.println("White label user link request successful");
        System.out.println("    QR Code URL: " + result.getQrCodeUrl());
        System.out.println("    Manual verification code: " + result.getCode());
        System.out.println();
    }

    private static void handleLogin(
            String username, String context, AuthService authService
    ) throws ApiException, InterruptedException {
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

    private static JSAP getJSAP() throws JSAPException {
        JSAP jsap = new JSAP();
        jsap.registerParameter(
                new Switch("help")
                        .setShortFlag('h')
                        .setLongFlag("help")
                        .setHelp("Get help on using this application")
        );
        jsap.registerParameter(
                new Switch("staging")
                        .setShortFlag('s')
                        .setLongFlag("staging")
                        .setHelp("Connect to the staging environment")
        );
        jsap.registerParameter(
                new UnflaggedOption("app_key")
                        .setUsageName("APP_KEY")
                        .setRequired(true)
                        .setHelp("Application Key for the application from Dashboard")
        );
        jsap.registerParameter(
                new UnflaggedOption("secret_key")
                        .setUsageName("SECRET_KEY")
                        .setRequired(true)
                        .setHelp("Secret Key for the application from the Dashboard")
        );
        jsap.registerParameter(
                new UnflaggedOption("private_key_file")
                        .setUsageName("PRIVATE_KEY_FILE")
                        .setRequired(true)
                        .setHelp(
                                "File location of the private key file for the application from the Dashboard"
                        )
        );
        jsap.registerParameter(
                new UnflaggedOption("command")
                        .setUsageName("COMMAND")
                        .setRequired(true)
                        .setHelp("Command to execute.  [login, authorized, logout, white-label-user-link]")
        );
        jsap.registerParameter(
                new UnflaggedOption("command-options")
                        .setUsageName("COMMAND_OPTION")
                        .setList(true)
                        .setGreedy(true)
                        .setListSeparator(' ')
                        .setHelp("Options for the command to execute")
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
