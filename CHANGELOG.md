# CHANGELOG

This changelog references the relevant changes (bug and security fixes) for the lifetime of the library.

  * 4.3.0

    * Added ability to enable jailbreak protection to authorization policy constructor.
    * Fixed bug in authorization policy the swapped inherence and knowledge factors.
    * Fixed bug where response headers were not being properly validated and verified.
    * Added verification of webhook headers.
    * Added dynamic authorization request title

  * 4.2.1

    * Fixed bug in Jose4jJWTService#decode which caused null pointer exception when decoding a request JWT from a webhook

  * 4.2.0
  
    * Add Organization Service management
    * Add Directory management
    * Add Directory Service management
    * Add Jail Break Protection to Auth Service Policy
    * Remove UUID version 1 requirement in factories for Organization, Directory, and Service ID as they can now be version 3 or 4
    * Add more specific named errors for HTTP status codes and LaunchKey error codes.
    * Migrate test mocks from Mockito 1.x to 2.x
    * Cucumber based integration tests
    * Dropped support for Java 6

  * 4.1.0
  
    * Add created and updated attributes to Device domain object
    * Fixed bug with example CLI app looking for incorrect Directory command argument
    * Updated help information to correctly identify that the Device ID is needed not the name when using hte device-unlink command
    * Add UUID validation to factories and clients for entity IDs
    * Fixed a few parameter names

  * 4.0.1

    Bug fix for Organization Factory method access

    * Make factory methods public
    * Add tests outside the package for all client factories to prevent recurrence

  * 4.0.0

    __If you are not embedding a LaunchKey authenticator in your own mobile application, you will not be able to use version 4.0.x. Future releases will provide v3 API access for implementations utilizing the LaunchKey Mobile Authenticator.__ 

    * Remove all API v1 endpoint code
    * Remove all newly deprecated API v3 endpoint code (organization endpoints)
    * Create clients and services for new API v3 endpoints
    * Rename all classes for migration to com.iovation.launchkey group

  * 3.1.0
    * Add organization based White Label Device management
    * Deprecate application based White Label Device linking
    * Create separate clients for organization and application credentials with a new factory
    * Deprecate old client and related classes
    * Implement JWT/JWE based communication with new organization endpoints

  * 3.0.0
    * Remove deprecated methods and classes
    * Update any documentation for new terminology

  * 2.1.0
    * Refactor classes, interfaces, methods, attributes, and parameters for new terminology. Backwards compatibility
        was maintained and old classes, interfaces, and methods were deprecated. They will be removed in version 3.0.
    * Add context to authorize and login
    * Add dynamic policy support

  * 2.0.3
    * Update users call in ApacheHttpTransport for proper handling of error responses
    * Update doc blocks for new build requirements
    * Bump nexus-staging-maven-plugin to be able to release under Java 8

  * 2.0.2
    * Update JCE Crypto to allow for creating Public/Private keys with PEM formatted files having Linux (\n) and
        Windows (\r\n) line ending styles.

  * 2.0.1
    * Fix POM for Spring MVC Example to require correct version of spring-security-core

  * 2.0.0
    * Make client easier to create with factories
    * Make client more extensible
    * Make crypto more testable
    * Separate transport and client to prepare for API v2
    * Move examples and SDK into one code base

  * 1.2.1
    * Update AuthenticationManager.createWhiteLabelUser() to allow for no identifier in response to enable forwards
        compatibility with new API change forthcoming.

  * 1.2.0
    * Update AuthenticationManager.createWhiteLabelUser() for new API implementation
    * Move more cryptography functions to Crypto helper class
    * Allow usage of RSA keys with header data
    * Mark Bouncy Castle as provided in POM.  Can't be used in the unsigned JAR anyway.

  * 1.1.1
    * Remove requirement for providing time for isAuthorized method in authentication manager.  It will use
        the time from the ping call it performs before it checks authorization. Authentication manager method
        isAuthorized(String, String) is now deprecated.  It will simply call the isAuthorized(String) and ignore the
        time provided.

  * 1.1.0
    * Added white label user create method
    * Loosened restrictions on dependency versions
    * Refactored to use non-deprecated methods for Apache HTTP client
    * Added ability to connect to staging servers for pre-release testing
    * Fixed bug in authentication manager that would error looking for a user_push_id response attribute when login call
        specified false for userPushId parameter

  * 1.0.0
    * First release with authentication methods
