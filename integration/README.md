# iovation LaunchKey SDK for Java - Integration Tests

The LaunchKey SDK for Java in a large and remarkably complex set of code. As such, it requires a battery of integration
tests to validate it is in good working order. The integration tests are BDD tests utilizing
[Cucumber for Java](https://cucumber.io/docs/reference/jvm#java).

  * [Installation](#installation)
  * [Usage](#usage)
  * [Help](#help)


## <a name="installation"></a>Installation

Execute the Maven goal of _package_. Executing _clean_ as well is never a bad idea.  For example:

```
mvn clean install
mvn clean package
```

If testing against a SNAPSHOT or changes that you've made locally, first, in this repository's root directory run 
`mvn clean install`. Then `cd` into this directory (`integration`) and run `mvn package`. Your tests should
then be ready to run.

## <a name="usage"></a>Usage

All of the required tests and libraries use the JAR similar to 
`sdk-integration-tests-4.5.0-SNAPSHOT-jar-with-dependencies.jar`. Replace that value with the name and location
of the JAR file you built via Maven. By default it will be in the `target`
directory directly under the directory where this file is located.

### <a name="prerequisites"></a>Prerequisites

In order to run the integration tests, a valid Organization with an active Public/Private Key Pair must exist. It is 
suggested that you use a test organization that is separate from any Organizations you use in production. Creation of
Organizations is managed by [Admin Center](https://admin.launchkey.com).

### <a name="run"></a>Run
  
Running the full suite of integration tests can be run by executing the JAR and including the following environment
values:

* Required
    * `Launchkey.Organization.id` - Organization ID from Admin Center.
    * `Launchkey.Organization.private_key` - File name of the PEM formatted RSA private key of the Public/Private Key Pair
    of the Organization with the ID in the `Launchkey.Organization.id` property.
* Optional
    * `Launchkey.API.base_url` - Base URL for the LaunchKey API. This will only be required if you are making changes
        to the SDK for pre-release functionality 

Example:
```
java \
-DLaunchkey.Organization.id=6ee17b28-bf8b-11e7-9b28-0469f8dc10a5 \
-DLaunchkey.Organization.private_key=/tmp/private-key.pem \
-jar target/sdk-integration-tests-4.6.0-SNAPSHOT-jar-with-dependencies.jar
classpath:features \
--glue classpath:com.iovation.launchkey.sdk.integration \
--plugin pretty
```

Example (With Emulator Based Device Tests):
 
 ```
 java \
 -DLaunchkey.Organization.id=6ee17b28-bf8b-11e7-9b28-0469f8dc10a5 \
 -DLaunchkey.Organization.private_key=/tmp/private-key.pem \
 -DAppium.url=https://localhost:4723/wd/hub \
 -jar sdk-integration-tests-4.5.0-SNAPSHOT-jar-with-dependencies.jar \
 classpath:features \
 --glue classpath:com.iovation.launchkey.mobile.integration \
 --plugin pretty \
 --plugin html:target/cucumber-htmlreport \
 --plugin json:target/cucumber-report.json
 ```

Example (With Kobiton Based Device Tests):

```
java \
-DLaunchkey.Organization.id=6ee17b28-bf8b-11e7-9b28-0469f8dc10a5 \
-DLaunchkey.Organization.private_key=/tmp/private-key.pem \
-DAppium.url=https://billy.jean:5e449dd9-2720-4767-babb-cf29eddecb94@api.kobiton.com/wd/hub \
-DAppium.Kobiton.use_kobiton=true \
-DAppium.Kobiton.auth=billy.jean:5e449dd9-2720-4767-babb-cf29eddecb94 \
-jar sdk-integration-tests-4.5.0-SNAPSHOT-jar-with-dependencies.jar \
classpath:features \
--glue classpath:com.iovation.launchkey.mobile.integration \
--plugin pretty \
--plugin html:target/cucumber-htmlreport \
--plugin json:target/cucumber-report.json
```

### <a name="help"></a>Help
  
Help can be obtained by either executing the application without any parameters or with -h.

Example:
```
java -jar sdk-integration-tests-4.5.0-SNAPSHOT-jar-with-dependencies.jar -h
```
