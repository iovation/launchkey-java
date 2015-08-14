package com.launchkey.example.cli;

import com.launchkey.sdk.LaunchKeyClient;
import com.launchkey.sdk.service.auth.AuthResponse;
import com.launchkey.sdk.service.auth.AuthService;
import com.launchkey.sdk.service.error.ExpiredAuthRequestException;
import com.launchkey.sdk.service.error.LaunchKeyException;
import com.launchkey.sdk.service.whitelabel.PairResponse;
import com.launchkey.sdk.service.whitelabel.WhiteLabelService;
import com.martiansoftware.jsap.*;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Security;
import java.util.Date;

/**
 * Copyright 2015 LaunchKey, Inc.  All rights reserved.
 *
 * Licensed under the MIT License.
 * You may not use this file except in compliance with the License.
 * A copy of the License is located in the "LICENSE.txt" file accompanying
 * this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class LaunchKeySdkDemoApp {

    public static void main(String[] args) {

        String privateKeyFile = null;
        try {
            JSAP jsap = getJSAP();

            JSAPResult commandLine = jsap.parse(args);
            String rocketKey = commandLine.getString("rocket_key");
            String secretKey = commandLine.getString("secret_key");
            privateKeyFile = commandLine.getString("private_key_file");

            if (commandLine.getBoolean("help") || rocketKey == null || secretKey == null || privateKeyFile == null) {
                printHelp(jsap);
                return;
            }

            LaunchKeyClient client = LaunchKeyClient.factory(
                    Long.parseLong(rocketKey),
                    secretKey,
                    readFile(privateKeyFile),
                    new BouncyCastleProvider()
            );

            String command = commandLine.getString("command");
            String[] commandOptions = commandLine.getStringArray("command-options");
            if (command.equalsIgnoreCase("login")) {
                if (commandOptions.length == 1) {
                    handleLogin(commandOptions[0], client.auth());
                } else {
                    System.out.println();
                    System.out.println(
                            "The login command takes a single command option which is the username with which to perform a login"
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
                            "The authorized command takes a single command option which is the Auth Request value returned from a login command"
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
                            "The authorized command takes a single command option which is the Auth Request value returned from a login command"
                    );
                    printHelp(jsap);
                    System.out.println();
                }
            } else if (command.equalsIgnoreCase("white-label-user-pair")) {
                if (commandOptions.length == 1) {
                    handleWhiteLabelPairUser(commandOptions[0], client.whiteLabel());
                } else {
                    System.out.println();
                    System.out.println(
                            "white-label-user-pair command requires a single argument of the unique ID for your application"
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
        } catch (LaunchKeyException e) {
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

    private static void handleWhiteLabelPairUser(String identifier, WhiteLabelService whiteLabelService) throws LaunchKeyException {
        PairResponse result = whiteLabelService.pairUser(identifier);
        System.out.println();
        System.out.println("White label user pair request successful");
        System.out.println("    QR Code URL: " + result.getQrCodeUrl());
        System.out.println("    Manual verification code: " + result.getCode());
        System.out.println();
    }

    private static void handleLogin(String username, AuthService authService) throws LaunchKeyException, InterruptedException {
        String authRequest = authService.login(username);
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
        String jar = new File(LaunchKeySdkDemoApp.class.getProtectionDomain().getCodeSource().getLocation().getPath())
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
                new UnflaggedOption("rocket_key")
                        .setUsageName("ROCKET_KEY")
//                        .setStringParser(JSAP.INTEGER_PARSER)
                        .setRequired(true)
                        .setHelp("Rocket Key for the application from the LaunchKey dashboard")
        );
        jsap.registerParameter(
                new UnflaggedOption("secret_key")
                        .setUsageName("SECRET_KEY")
                        .setRequired(true)
                        .setHelp("Secret Key for the application from the LaunchKey dashboard")
        );
        jsap.registerParameter(
                new UnflaggedOption("private_key_file")
                        .setUsageName("PRIVATE_KEY_FILE")
                        .setRequired(true)
                        .setHelp(
                                "File location of the private key file for the application from the LaunchKey dashboard"
                        )
        );
        jsap.registerParameter(
                new UnflaggedOption("command")
                        .setUsageName("COMMAND")
                        .setRequired(true)
                        .setHelp("Command to execute.  [login, authorized, logout, white-label-pair-user]")
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