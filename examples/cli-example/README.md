# iovation LaunchKey SDK for Java - CLI Example


  * [Pre-Requisites](#prerequisites)
  * [Installation](#installation)
  * [Usage](#usage)
    * [Help](#help)
    * [Application User Session Management](#app)
    * [White Label Device Management](#whitelabel)

## <a name="prerequisites"></a>Pre-Requisites

Follow the pre-requisites instructions for the iovation LaunchKey SDK for Java: [SDK Instructions](../../sdk/README.md#prerequisites).
The demo will utilize the Bouncy Castle JCE provider.  Make sure to install that provider in the instructions.

## <a name="installation"></a>Installation

Execute the Maven goal of _package_. Executing _clean_ as well is never a bad idea.  For example:

```
mvn clean package
```

## <a name="usage"></a>Usage

All of the supplied examples use the JAR of examples-cli-4.0.0.jar. Replace that value with the name and location
of the JAR file you downloaded or built via Maven. If you build the package with Maven, it will be in the `target`
directory directly under the directory where this file is located.

### <a name="help"></a>Help
  
Help can be obtained by either executing the application without any parameters or with -h.

```
java -jar examples-cli-4.0.0-jar-with-dependencies.jar -h
```

Help for command can be obtained in a similar manner while including the command.

```
java -jar examples-cli-4.0.0-jar-with-dependencies.jar -h application

```

### <a name="commands"></a>Commands

There are two commands which have a number of actions they can perform.

  * [service](#service)
    * authorize
    * session-start
    * session-end
  * [directory](#directory)
    * device-link
    * devices-list
    * device-unlink

### <a name="service"></a>Service User Session Management

The example CLI provides the ability to manage a user session for an Application. Applications are managed utilizing
application credentials for the Application ID you provide in the calls.

  1. Login
  
        Performing an authorization request is accomplished with the authorize action which is passed a username.

        Example: Execute an authorization request for user _myusername_, the service ID of 
        _cc07872d-1f99-41e4-8359-eaff0d2269d9_, and service private key _/tmp/private.key_.

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        cc07872d-1f99-41e4-8359-eaff0d2269d9 /tmp/private.key authorize myusername
        ```

        The request will happen in two steps.  The service will make an authorization request and then poll for a user
        response:  That portion will look something like this:

        ```
        Authorization request successful
            Auth Request: 03b99174-c23d-4bff-ae50-d56de719d4d4
        Checking for response from user
        ..............
        ```

        One of three scenarios will occur.  The request will timeout or the user will accept or reject the request.  Here are
        examples of each:

        Request timed out:

        ```
        Login request timed out
        ```

        User accepted:

        ```
        Authorization request accepted by user
            Auth Request:  03b99174-c23d-4bff-ae50-d56de719d4d4
            Device ID:     e0SE
            Svc User Hash: B8luyaSwKdFnABYghEZdPLb3QI7RJ02yfYnAr67EPpi
            User Push ID:  eff2bb17-86e4-4a4b-8b0d-2e46b5d8609b
            Org User Hash: N/A
        ```

        User rejected:

        ```
        Authorization request rejected by user
        ```

  2. Start a user session 

        Pass the Username to the session-start action

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        cc07872d-1f99-41e4-8359-eaff0d2269d9 /tmp/private.key session-start myusername
        ```

        You will receive the message:

        ```
        User session is started.
        ```

  3. Ebd a user session
  
        Pass the Username to the session-end action

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        cc07872d-1f99-41e4-8359-eaff0d2269d9 /tmp/private.key session-end myusername
        ```

        You will receive the message:

        ```
        User session is ended.
        ```

### <a name="directory"></a>Directory User Device Management

Directory User devices are authenticators utilizing the Authenticator SDK.  The linking and unlinking of devices for 
users in your application can be achieved via "directory" commands.

Directory commands a performed utilizing credentials for the Directory.

  1. Linking a Device

        Pass a unique identifier for the end user in your system to the device-link action.  The example request
        below uses a UUID _326335b0-8569-4aa3-90a3-ac4372104ea3_ as the user identifier with the Directory whose ID is  _3cb7c699-be47-414f-830b-e81b9bb8cc40_
        and private key is located at _/tmp/private.key_.

        Request:
    
        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        3cb7c699-be47-414f-830b-e81b9bb8cc40 /tmp/private.key device-link 326335b0-8569-4aa3-90a3-ac4372104ea3

        ```

        Response when the credentials are invalid:

        ```
        There was an error executing your command: Invalid Credentials
        ```

        Response when Directory User is successfully created:

        ```
        Device link request successful
            QR Code URL: https://api.onprem.com/qrcode/5r53j9z
            Manual verification code: 5r53j9z
        ```


  2. Listing the Devices linked to a Directory User

        Pass a unique identifier for a user in your system to the devices-list action.  The example request
        below uses a UUID _326335b0-8569-4aa3-90a3-ac4372104ea3_ as the user identifier with the Directory whose ID is 
         _3cb7c699-be47-414f-830b-e81b9bb8cc40_ and private key is located at _/tmp/private.key_.

        Request:

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        3cb7c699-be47-414f-830b-e81b9bb8cc40 /tmp/private.key devices-list 326335b0-8569-4aa3-90a3-ac4372104ea3
        ```

        Response when the credentials are invalid:

        ```
        There was an error executing your command: Invalid Credentials: WL-DEV-P-4-03
        ```

        Response when your SDK is not valid:

        ```
        There was an error executing your command: Invalid Request: WL-DEV-P-4-00
        ```

        Response when White Label Group User is successfully created:

        ```
        Devices:
          device:
            Type:    iOS
            Status:  LINKED
          device2:
            Type:    Android
            Status:  LINKED
        ```


  3. Unlinking a Device

        Pass a unique identifier for the end user in your system to the device-unlink action.  The example request
        below uses a UUID _326335b0-8569-4aa3-90a3-ac4372104ea3_ as the user identifier with the Directory whose ID is 
        _3cb7c699-be47-414f-830b-e81b9bb8cc40_ and private key is located at _/tmp/private.key_.

        Request:
    
        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        3cb7c699-be47-414f-830b-e81b9bb8cc40 /tmp/private.key device-unlink 326335b0-8569-4aa3-90a3-ac4372104ea3

        ```

        Response when the credentials are invalid:

        ```
        There was an error executing your command: Invalid Credentials
        ```

        Response when Directory User is successfully created:

        ```
        Device unlinked
        ```


