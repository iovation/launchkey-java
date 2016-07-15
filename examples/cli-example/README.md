# SDK for Java - CLI Example


  * [Pre-Requisites](#prerequisites)
  * [Installation](#installation)
  * [Usage](#usage)
    * [Help](#help)
    * [Application User Session Management](#app)
    * [White Label Device Management](#whitelabel)

## <a name="prerequisites"></a>Pre-Requisites

Follow the pre-requisites instructions for the Platform SDK for Java: [SDK Instructions](../../sdk/README.md#prerequisites).
The demo will utilize the Bouncy Castle JCE provider.  Make sure to install that provider in the instructions.

## <a name="installation"></a>Installation

Execute the Maven goal of _package_. Executing _clean_ as well is never a bad idea.  For example:

```
mvn clean package
```

## <a name="usage"></a>Usage

All of the supplied examples use the JAR of examples-cli-3.0.0.jar. Replace that value with the name and location
of the JAR file you downloaded or built via Maven. If you build the package with Maven, it will be in the `target`
directory directly under the directory where this file is located.

### <a name="help"></a>Help
  
Help can be obtained by either executing the application without any parameters or with -h.

```
java -jar examples-cli-3.0.0-jar-with-dependencies.jar -h
```

Help for command can be obtained in a similar manner while including the command.

```
java -jar examples-cli-3.0.0-jar-with-dependencies.jar -h application

```

### <a name="commands"></a>Commands

There are two commands which have a number of actions they can perform.

  * [application](#app)
    * login
    * authorized
    * logout
  * [white-label](#white-label)
    * link-device
    * list-device
    * unlink-device

### <a name="app"></a>Application User Session Management

The example CLI provides the ability to manage a user session for an Application. Applications are managed utilizing
application credentials for the Application ID you provide in the calls.

  1. Login
  
        Performing a login request is accomplished with the login action which is passed a username.

        Example: Execute a login request for user _myusername_.

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        application 123456789 abcdefghijklmnopqrst /tmp/private.key login myusername
        ```

        The request will happen in two steps.  The application will make an authorization request and then poll for a user
        response:  That portion will look something like this:

        ```
        Login request successful
            Date Time: 2015-02-04 18:29:27
            Auth Request: wb9phc6kv2qg5ajplr13tpq8b5sx4ukn
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
        Login request accepted by user
            App PINs:     3722,861,8293,2115,2255
            Auth Request: wb9phc6kv2qg5ajplr13tpq8b5sx4ukn
            Device ID:    e0SE
            user Hash:    B8luyaSwKdFnABYghEZdPLb3QI7RJ02yfYnAr67EPpi
            User Push ID: null
        ```

        User rejected:

        ```
        Login request rejected by user
        ```

  2. Verify user is still authorized (has not de-orbited)

        Pass the _Auth Request_ value from the login request to the authorized action

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        application 123456789 abcdefghijklmnopqrst /tmp/private.key authorized wb9phc6kv2qg5ajplr13tpq8b5sx4ukn
        ```

        One of two scenarios will occur, authorized or unauthorized.  Here are examples of each:

        ```
        User is still authorized
        ```

        ```
        User is not authorized
        ```

  3. Log out a user
  
        Pass the _Auth Request_ value from the login request to the logout action

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        application 123456789 abcdefghijklmnopqrst /tmp/private.key logout wb9phc6kv2qg5ajplr13tpq8b5sx4ukn
        ```

        You should receive the following response:

        ```
        User is logged out.
        ```

### <a name="whitelabel"></a>White Label Device Management

White label devices are mobile applications utilizing the WhiteLabel SDK to embed the functionality of the
Mobile Authenticator.  The linking and unlinking of devices for users in your application can be achieved
via "white-label" commands.

White Label commands a performed utilizing credentials for the Organization whose Organizaiton the White Label Group
beliongs as defined by the SDK key.

  1. Linking a Device

        Pass a unique identifier for a user in your system to the device-link action.  The example request
        below uses a GUID and .

        Request:
    
        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        123456789 \
        SDKKEY \
        /tmp/private.key \
        device-link \
        326335b0-8569-4aa3-90a3-ac4372104ea3

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
        Device link request successful
            QR Code URL: https://api.onprem.com/qrcode/5r53j9z
            Manual verification code: 5r53j9z
        ```


  1. Linking a Device

        Pass a unique identifier for a user in your system to the devices-list action.  The example request
        below uses a GUID and .

        Request:

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        123456789 \
        SDKKEY \
        /tmp/private.key \
        devices-list \
        326335b0-8569-4aa3-90a3-ac4372104ea3
        myuser
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
            Type:    Dashboard
            Status:  LINKED
            Created: Wed Jul 13 12:54:11 PDT 2016
            Updated: Wed Jul 13 12:54:14 PDT 2016
          device2:
            Type:    Dashboard
            Status:  LINKED
            Created: Wed Jul 13 12:54:47 PDT 2016
            Updated: Wed Jul 13 12:54:49 PDT 2016
        ```


  1. Unlinking a Device

        Pass a unique identifier for a user in your system and the device name to the device-unlink action.  The example request
        below uses a GUID and .

        Request:

        ```
        java -jar examples-cli-3.0.0-jar-with-dependencies.jar \
        123456789 \
        SDKKEY \
        /tmp/private.key \
        device-link \
        326335b0-8569-4aa3-90a3-ac4372104ea3 \
        device1
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
        Device unlink request successful
        ```


