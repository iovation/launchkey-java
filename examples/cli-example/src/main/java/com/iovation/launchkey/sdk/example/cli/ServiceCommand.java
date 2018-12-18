package com.iovation.launchkey.sdk.example.cli;

import com.iovation.launchkey.sdk.client.ServiceClient;
import com.iovation.launchkey.sdk.domain.service.AuthorizationRequest;
import com.iovation.launchkey.sdk.domain.service.AuthorizationResponse;
import picocli.CommandLine;

import java.io.File;
import java.util.Date;
import java.util.Scanner;

@CommandLine.Command(name = "service")
public class ServiceCommand {

    @CommandLine.ParentCommand
    LaunchKeyCommand launchKeyCommand;

    @CommandLine.Parameters(index = "0", paramLabel = "<SVC_ID>",
            description = "Service ID. It is found in the Keys section of the Service's profile page in Dashboard.")
    String serviceId;

    @CommandLine.Parameters(index = "1", paramLabel = "<KEY_FILE>",
            description = "File location of the RSA Private Key of the RSA public/private key pair whose public key " +
                    "is associated with the Service.")
    File privateKeyFile;

    @CommandLine.Command(name = "session-start")
    void sessionStart(@CommandLine.Parameters(paramLabel = "<USER_NAME>") String username) throws Exception {
        getServiceClient().sessionStart(username);
        System.out.println();
        System.out.println("User session is started.");
        System.out.println();
    }

    @CommandLine.Command(name = "session-end")
    void sessionEnd(@CommandLine.Parameters(paramLabel = "<USER_NAME>") String username) throws Exception {
        getServiceClient().sessionEnd(username);
        System.out.println();
        System.out.println("User session is ended.");
        System.out.println();
    }

    @CommandLine.Command(name = "authorize")
    void authorize(@CommandLine.Parameters(paramLabel = "<USER_NAME>") String username,
                   @CommandLine.Parameters(paramLabel = "<CONTEXT>", arity = "0..1") String context) throws Exception {
        ServiceClient serviceClient = getServiceClient();
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

    private ServiceClient getServiceClient() throws Exception {
        Scanner sc = new Scanner(privateKeyFile);
        StringBuilder buffer = new StringBuilder();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()).append("\n");
        }
        String key = buffer.toString();

        return launchKeyCommand.getFactoryFactory()
                .makeServiceFactory(serviceId, key).makeServiceClient();
    }

    private static String naForNull(String value) {
        return value == null ? "N/A" : value;
    }

    private static String naForNull(Enum value) {
        return naForNull(value.name());
    }

    private static String naForNull(Boolean value) {
        return naForNull(value.toString());
    }
}
