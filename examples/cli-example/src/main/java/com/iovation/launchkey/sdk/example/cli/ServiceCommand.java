package com.iovation.launchkey.sdk.example.cli;

import com.iovation.launchkey.sdk.client.ServiceClient;
import com.iovation.launchkey.sdk.domain.policy.Fence;
import com.iovation.launchkey.sdk.domain.policy.GeoCircleFence;
import com.iovation.launchkey.sdk.domain.policy.TerritoryFence;
import com.iovation.launchkey.sdk.domain.service.*;
import com.iovation.launchkey.sdk.error.AuthorizationInProgress;
import com.iovation.launchkey.sdk.error.AuthorizationRequestCanceled;
import com.iovation.launchkey.sdk.error.AuthorizationRequestTimedOutError;
import picocli.CommandLine;

import java.io.File;
import java.nio.charset.Charset;
import java.util.*;

@SuppressWarnings("unused")
@CommandLine.Command(name = "service")
class ServiceCommand {

    @CommandLine.ParentCommand
    private RootCommand rootCommand;

    @CommandLine.Parameters(index = "0", paramLabel = "<SVC_ID>",
            description = "Service ID. It is found in the Keys section of the Service's profile page in Dashboard.")
    private String serviceId;

    @CommandLine.Parameters(index = "1", paramLabel = "<KEY_FILE>",
            description = "File location of the RSA Private Key of the RSA public/private key pair whose public key " +
                    "is associated with the Service.")
    private File privateKeyFile;

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
    void authorize(
            @CommandLine.Parameters(paramLabel = "<USER_NAME>",
                    description = "The username to authorize") String username,
            @CommandLine.Parameters(paramLabel = "<CONTEXT>", arity = "0..1",
                    description = "The context value of the authorization request.") String context,
            @CommandLine.Option(names = {"-t", "--title"}, arity = "0..1",
                    description = "[Directory Service Only] Title of the authorization request.") String title,
            @CommandLine.Option(names = {"-l", "--ttl"}, arity = "0..1",
                    description = "Time to Live in seconds for the request. This must be between 30 and 600.") Integer ttl,
            @CommandLine.Option(names = {"-h", "--push-title"}, arity = "0..1",
                    description = "[Directory Service Only] Title of the push message for the authorization request.") String pushTitle,
            @CommandLine.Option(names = {"-b", "--push-body"}, arity = "0..1",
                    description = "[Directory Service Only] Body of the push message for the authorization request.") String pushBody,
            @CommandLine.Option(names = {"-f", "--fraud-denial-reasons"}, arity = "0..1",
                    description = "[Directory Service Only] The number of denial reasons flagged as fraud for the authorization request.") Integer fraudReasons,
            @CommandLine.Option(names = {"-n", "--non-fraud-denial-reasons"}, arity = "0..1",
                    description = "[Directory Service Only] The number of denial reasons not flagged as fraud for the authorization request.") Integer nonFraudReasons
    ) throws Exception {
        try {
            ServiceClient serviceClient = getServiceClient();
            AuthorizationRequest authRequest = serviceClient.createAuthorizationRequest(
                    username, context, null, title, ttl, pushTitle, pushBody, getDenialReasons(fraudReasons, nonFraudReasons));
            System.out.println();
            System.out.println("Authorization request successful");
            System.out.println("    Auth Request: " + authRequest);
            System.out.print("Checking for response from the End User");
            System.out.println();
            AdvancedAuthorizationResponse authorizationResponse;
            long started = new Date().getTime();
            while (new Date().getTime() - started <= 600000) {
                    Thread.sleep(1000L);
                    System.out.print(".");
                    authorizationResponse = serviceClient.getAdvancedAuthorizationResponse(authRequest.getId());
                    if (authorizationResponse != null) {
                        System.out.println("Authorization request response received:");
                        System.out.println("    Request ID:    " + authorizationResponse.getAuthorizationRequestId());
                        System.out.println("    Authorized:    " + safeNull(authorizationResponse.isAuthorized()));
                        System.out.println("    Type:          " + safeNull(authorizationResponse.getType()));
                        System.out.println("    Reason:        " + safeNull(authorizationResponse.getReason()));
                        System.out.println("    Denial Reason: " + safeNull(authorizationResponse.getDenialReason()));
                        System.out.println("    Fraud:         " + safeNull(authorizationResponse.isFraud()));
                        System.out.println("    Device ID:     " + authorizationResponse.getDeviceId());
                        System.out.println("    Svc User Hash: " + authorizationResponse.getServiceUserHash());
                        System.out.println("    User Push ID:  " + authorizationResponse.getUserPushId());
                        System.out.println("    Org User Hash: " + safeNull(authorizationResponse.getOrganizationUserHash()));

                        System.out.print("    Auth Policy: ");
                        AuthorizationResponsePolicy policy = authorizationResponse.getPolicy();
                        if (policy == null) {
                            System.out.println("None");
                        } else {
                            System.out.println();
                            System.out.println("        Factors:    " + safeNull(policy.getAmount()));
                            System.out.println("        Inherence:  " + safeNull(policy.wasInherenceRequired()));
                            System.out.println("        Knowledge:  " + safeNull(policy.wasKnowledgeRequired()));
                            System.out.println("        Possession: " + safeNull(policy.wasPossessionRequired()));

                            System.out.print("        Geofences: ");
                            List<Fence> fences = authorizationResponse.getPolicy().getFences();
                            if (fences == null || fences.size() < 1) {
                                System.out.println("None");
                            } else {
                                System.out.println();
                                for (Fence fence : fences) {
                                    System.out.println("            Name: " + safeNull(fence.getName()));
                                    if (fence instanceof GeoCircleFence) {
                                        GeoCircleFence circleFence = (GeoCircleFence) fence;
                                        System.out.println("                Radius:    " + safeNull(circleFence.getRadius()));
                                        System.out.println("                Latitude:  " + safeNull(circleFence.getLatitude()));
                                        System.out.println("                Longitude: " + safeNull(circleFence.getLongitude()));
                                    } else if (fence instanceof TerritoryFence) {
                                        TerritoryFence territoryFence = (TerritoryFence) fence;
                                        System.out.println("                Radius:    " + safeNull(territoryFence.getCountry()));
                                        System.out.println("                Latitude:  " + safeNull(territoryFence.getAdministrativeArea()));
                                        System.out.println("                Longitude: " + safeNull(territoryFence.getPostalCode()));

                                    } else {
                                        System.out.println("                Unhandled Type:    " + fence.getClass().getSimpleName());
                                    }
                                }
                            }
                        }

                        System.out.print("    Auth Methods: ");
                        List<AuthMethod> methods = authorizationResponse.getAuthMethods();
                        if (methods == null || methods.size() < 1) {
                            System.out.println("None");
                        } else {
                            System.out.println();
                            for (AuthMethod method : methods) {
                                System.out.println("        Method: " + method.getMethod().name());
                                System.out.println("            Set:        " + safeNull(method.getSet()));
                                System.out.println("            Active:     " + safeNull(method.getActive()));
                                System.out.println("            Allowed:    " + safeNull(method.getAllowed()));
                                System.out.println("            Supported:  " + safeNull(method.getSupported()));
                                System.out.println("            Required:   " + safeNull(method.getUserRequired()));
                                System.out.println("            Passed:     " + safeNull(method.getPassed()));
                                System.out.println("            Error:      " + safeNull(method.getError()));
                            }
                        }
                        break;
                    }
            }
        } catch (AuthorizationRequestTimedOutError e) {
            System.out.println();
            System.out.println("Authorization request timed out!");
        } catch (AuthorizationRequestCanceled e) {
            System.out.println();
            System.out.println("Authorization request was cancelled!");
        } catch (AuthorizationInProgress e) {
            System.out.println();
            System.out.println("Authorization already in progress!");
            System.out.println("    Auth Request: " + e.getAuthorizationRequestId());
            System.out.println("    Expires:      " + e.getExpires());
            System.out.println("    Same Service: " + e.isFromSameService());
        }

        System.out.println();
    }

    @CommandLine.Command(name = "cancel-auth-request")
    void cancelAuthRequest(@CommandLine.Parameters(paramLabel = "<AUTH_REQUEST>") String authRequestId) throws Exception {
        getServiceClient().cancelAuthorizationRequest(authRequestId);
        System.out.println();
        System.out.println("Auth Cancelled.");
        System.out.println();
    }

    private ServiceClient getServiceClient() throws Exception {
        Scanner sc = new Scanner(privateKeyFile);
        StringBuilder buffer = new StringBuilder();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()).append("\n");
        }
        String key = buffer.toString();

        return rootCommand.getFactoryFactory()
                .makeServiceFactory(serviceId, key).makeServiceClient();
    }

    private static String safeNull(String value) {
        return value == null ? "None" : value;
    }

    private static String safeNull(Enum value) {
        String name = value == null ? null : value.name();
        return safeNull(name);
    }

    private static String safeNull(Boolean value) {
        String name = value == null ? null : value.toString();
        return safeNull(name);
    }

    private static String safeNull(Integer value) {
        String name = value == null ? null : value.toString();
        return safeNull(name);
    }

    private static String safeNull(Double value) {
        String name = value == null ? null : value.toString();
        return safeNull(name);
    }

    private static List<DenialReason> getDenialReasons(Integer fraud, Integer nonFraud)
    {
        List<DenialReason> denialReasons;
        if (fraud == null && nonFraud == null) {
            denialReasons = null;
        } else {
            fraud = fraud == null ? 0 : fraud;
            nonFraud = nonFraud == null ? 0 : fraud;
            denialReasons = new ArrayList<>();
            for (int i = 0; i < Math.max(fraud, nonFraud); i++)
            {
                if (i < fraud)
                {
                    String reason = getRandomString();
                    String id = "F" + i;
                    denialReasons.add(new DenialReason(id, reason + " - " + id, true));
                }
                if (i < nonFraud)
                {
                    String reason = getRandomString();
                    String id = "NF" + i;
                    denialReasons.add(new DenialReason(id, reason + " - " + id, false));
                }
            }
        }
        return denialReasons;
    }

    private static String getRandomString() {
        Random random = new Random();
        byte[] array = new byte[random.nextInt(6)];
        random.nextBytes(array);
        return new String(array, Charset.forName("UTF-8"));
    }
}
