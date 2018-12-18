package com.iovation.launchkey.sdk.example.cli;

import com.iovation.launchkey.sdk.client.DirectoryClient;
import com.iovation.launchkey.sdk.domain.directory.Device;
import com.iovation.launchkey.sdk.domain.directory.DirectoryUserDeviceLinkData;
import picocli.CommandLine;

import java.io.File;
import java.util.List;
import java.util.Scanner;

@CommandLine.Command(name = "directory")
class DirectoryCommand {

    @CommandLine.ParentCommand
    private RootCommand rootCommand;

    @CommandLine.Parameters(index = "0", paramLabel = "<DIR_ID>",
            description = "Directory ID. It is found in the Keys section of the Directory's page in Dashboard.")
    private String directoryId;

    @CommandLine.Parameters(index = "1", paramLabel = "<KEY_FILE>",
            description = "File location of the RSA Private Key of the RSA public/private key pair whose public key " +
                    "is associated with the Directory.")
    private File privateKeyFile;

    @CommandLine.Command(name = "device-link")
    void deviceLink(@CommandLine.Parameters(paramLabel = "<UNIQUE_IDENTIFIER>",
            description = "Unique identifier of the user for your application") String identifier) throws Exception {
        DirectoryUserDeviceLinkData result = getDirectoryClient().linkDevice(identifier);
        System.out.println();
        System.out.println("Device link request successful");
        System.out.println("    QR Code URL: " + result.getQrCodeUrl());
        System.out.println("    Manual verification code: " + result.getCode());
        System.out.println();
    }

    @CommandLine.Command(name = "device-unlink")
    void deivceUnlink(
            @CommandLine.Parameters(paramLabel = "<UNIQUE_IDENTIFIER>",
                    description = "Unique identifier of the user for your application") String identifier,
            @CommandLine.Parameters(paramLabel = "<DEVICE_ID>",
                    description = "Identifier of the Device you wish to unink") String deviceId) throws Exception {
        System.out.println();
        getDirectoryClient().unlinkDevice(identifier, deviceId);
        System.out.println("Device unlinked");
        System.out.println();
    }

    @CommandLine.Command(name = "devices-list")
    void devicesList(@CommandLine.Parameters(paramLabel = "<UNIQUE_IDENTIFIER>",
            description = "Unique identifier of the user for your application") String identifier) throws Exception {
        List<Device> devices = getDirectoryClient().getLinkedDevices(identifier);
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

    private DirectoryClient getDirectoryClient() throws Exception {
        Scanner sc = new Scanner(privateKeyFile);
        StringBuilder buffer = new StringBuilder();
        while (sc.hasNextLine()) {
            buffer.append(sc.nextLine()).append("\n");
        }
        String key = buffer.toString();

        return rootCommand.getFactoryFactory()
                .makeDirectoryFactory(directoryId, key).makeDirectoryClient();
    }
}
