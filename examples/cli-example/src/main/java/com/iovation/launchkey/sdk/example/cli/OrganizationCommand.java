package com.iovation.launchkey.sdk.example.cli;

import com.iovation.launchkey.sdk.client.OrganizationClient;
import picocli.CommandLine;

import java.io.File;
import java.net.URI;
import java.util.Scanner;
import java.util.UUID;

@CommandLine.Command(name = "organization")
class OrganizationCommand {

    @CommandLine.ParentCommand
    private RootCommand rootCommand;

    @CommandLine.Parameters(index = "0", paramLabel = "<ORG_ID>",
            description = "Organization ID. It is found in the Keys section of the Organization's page in Dashboard.")
    private String organizationId;

    @CommandLine.Parameters(index = "1", paramLabel = "<KEY_FILE>",
            description = "File location of the RSA Private Key of the RSA public/private key pair whose public key " +
                    "is associated with the Organization.")
    private File privateKeyFile;

    @CommandLine.Command(name = "update-directory")
    void updateDirectory(
            @CommandLine.Parameters(paramLabel = "<DIRECTORY_ID>",
                    description = "URL for webhooks to contact for Directory level events") String directoryId,
            @CommandLine.Parameters(paramLabel = "<WEBHOOK_URL>",
                    description = "URL for webhooks to contact for Directory level events") String callback_url
    ) throws Exception {
        getOrganizationClient().updateDirectory(UUID.fromString(directoryId), null, null, null, null, URI.create(callback_url));
        System.out.println();
        System.out.println("Directory update successful");
        System.out.println();
    }

    private OrganizationClient getOrganizationClient() throws Exception {
        Scanner sc = new Scanner(privateKeyFile);
        StringBuilder buffer = new StringBuilder();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()).append("\n");
        }
        String key = buffer.toString();

        return rootCommand.getFactoryFactory()
                .makeOrganizationFactory(organizationId, key).makeOrganizationClient();
    }
}
