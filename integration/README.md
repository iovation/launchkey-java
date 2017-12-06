# iovation LaunchKey SDK for Java - Integration Tests

The LaunchKey SDK for Java in a large and remarkably complex set of code. As such, it requires a battery of integration
tests to validate it is in good working order. The integration tests are BDD tests utilizing
[Cucumber for Java](https://cucumber.io/docs/reference/jvm#java).

  * [Installation](#installation)
  * [Usage](#usage)


## <a name="installation"></a>Installation

Execute the Maven goal of _package_. Executing _clean_ as well is never a bad idea.  For example:

```
mvn clean package
```

## <a name="usage"></a>Usage

All of the required tests and libraries use the JAR similar to 
`sdk-integration-tests-4.2.0-SNAPSHOT-jar-with-dependencies.jar`. Replace that value with the name and location
of the JAR file you built via Maven. By default it will be in the `target`
directory directly under the directory where this file is located.

### <a name="prerequisites></a>Prerequisites

In order to run the integration tests, a valid Organization with an active Public/Private Key Pair must exist. It is 
suggested that you use a test organization that is separate from any Organizations you use in production. Creation of
Organizations is managed by [Admin Center](https://admin.launchkey.com).  

### <a name="help"></a>Help
  
Running the full suite of integration tests can be run by executing the JAR and including the following environment
values:

* `lk.organization.id` - Organization ID from Admin Center.
* `lk.organization.private_key` - File name of the PEM formatted RSA private key of the Public/Private Key Pair
    of the Organization with the ID in the `lk.organization.id` property.
* _(OPTIONAL)_ `lk.api.base_url` - Base URL for the LaunchKey API. This will only be applicable for LaunchKey 
    developers.
 
Example:
```
java -jar sdk-integration-tests-4.2.0-SNAPSHOT-jar-with-dependencies.jar -Dlk.organization.id=6ee17b28-bf8b-11e7-9b28-0469f8dc10a5 -Dlk.organization.private_key=/tmp/private-key.pem
```

### <a name="help"></a>Help
  
Help can be obtained by either executing the application without any parameters or with -h.

Example:
```
java -jar sdk-integration-tests-4.2.0-SNAPSHOT-jar-with-dependencies.jar -h
```
