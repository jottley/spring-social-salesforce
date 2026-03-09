# Spring Salesforce

[![Java CI with Maven](https://github.com/jottley/spring-salesforce/actions/workflows/maven.yml/badge.svg)](https://github.com/jottley/spring-salesforce/actions/workflows/maven.yml)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)

Spring Salesforce is a Spring extension that provides connection support and API binding for Salesforce REST API. It simplifies integration with Salesforce by providing a clean, Spring-native interface to interact with Salesforce data and services.

## Features

- 🔐 OAuth2 authentication support (with or without Spring Security)
- 🚀 Comprehensive REST API operations
- 🔄 SOQL query and SOSL search support
- 📊 sObject CRUD operations
- 🎯 Custom Apex REST API integration
- 💬 Chatter feed operations
- 🏘️ Communities (Digital Experience) support
- ⚡ API version management and deprecation handling

## Requirements

- **Java**: 17 or higher
- **Spring Framework**: 7.0+
- **Spring Security**: 7.0+ (optional, for OAuth2 integration)
- **Salesforce API**: v62.0+ (earlier versions supported with potential limitations)
- **Maven**: 3.6+ (for building from source)

## Installation

### Maven

Add the following repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>spring-salesforce</id>
        <url>https://repo.repsy.io/mvn/jottley/spring-salesforce</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-salesforce</artifactId>
        <version>2.0.1</version>
    </dependency>
</dependencies>
```

## Quick Start

### Basic Usage

```java
// Create Salesforce connection with OAuth2 access token
Salesforce salesforce = new SalesforceTemplate(accessToken);
salesforce.setInstanceUrl("https://your-instance.salesforce.com");

// Query records
QueryResult result = salesforce.queryOperations()
    .query("SELECT Id, Name FROM Account LIMIT 10");

// Create a new record
Map<String, Object> account = new HashMap<>();
account.put("Name", "Acme Corporation");
String recordId = salesforce.sObjectsOperations()
    .create("Account", account);

// Update an existing record
Map<String, Object> updates = new HashMap<>();
updates.put("Phone", "+1-555-0100");
salesforce.sObjectsOperations()
    .update("Account", recordId, updates);

// Retrieve a record
Map<String, Object> record = salesforce.sObjectsOperations()
    .getRow("Account", recordId);
```

### Custom Apex REST API

```java
// Call custom Apex REST endpoint
MyCustomResponse response = salesforce.customApiOperations()
    .getForApexObject("/MyCustomAPI/v1/data", MyCustomResponse.class);

// POST to custom Apex REST endpoint
MyRequest request = new MyRequest("data");
MyCustomResponse result = salesforce.customApiOperations()
    .postForApexObject("/MyCustomAPI/v1/create", request, MyCustomResponse.class);
```

For a complete working example with Spring Boot and OAuth2 configuration, see the [Spring Salesforce Quickstart](https://github.com/jottley/spring-salesforce-quickstart) repository.

## Supported Operations

### ApiOperations
- Retrieve all available API versions
- Retrieve services supported by a specific API version
- API version validation and deprecation warnings

### SObjectOperations
- Retrieve the list of sObjects
- Retrieve summary metadata of a sObject
- Retrieve full metadata of a sObject
- Retrieve a row from a sObject
- Retrieve a blob from a row in a sObject
- Create a new sObject
- Update an existing sObject
- Delete an sObject

### QueryOperations
- Run SOQL queries and retrieve results (with paging or all records)
- Query results can optionally include deleted records
- Support for relationship queries (parent-to-child, child-to-parent)

### SearchOperations
- Execute SOSL searches and retrieve results (with paging or all records)

### RecentOperations
- Retrieve recent changes feed

### ChatterOperations
- Retrieve current user's profile
- Retrieve user status
- Update user status

### UserOperations
- Retrieve user profile information

### LimitsOperations
- List the limits of an org
- Check current API limit and usage

### ConnectOperations
- Get a list of Communities (Digital Experience)
- Get a list of Community (Digital Experience) users

### CustomApiOperations
- Call custom Apex REST APIs (GET, POST, PUT, PATCH, DELETE)
- Support for URI variables and request/response type mapping
- Access to full HTTP response (headers and body)

## Error Handling

Spring Salesforce provides specific exception types for common Salesforce API errors:

- **`InvalidAuthorizationException`** - Authentication failed or token expired
- **`RateLimitExceededException`** - API rate limit exceeded
- **`ResourceNotFoundException`** - Requested resource not found (404)
- **`InsufficientPermissionException`** - User lacks required permissions (403)
- **`DeprecatedApiVersionException`** - API version is no longer supported (410)
- **`UncategorizedApiException`** - Other API errors

Example error handling:

```java
try {
    QueryResult result = salesforce.queryOperations().query("SELECT Id FROM Account");
} catch (RateLimitExceededException e) {
    // Handle rate limiting - implement backoff/retry logic
    log.warn("Rate limit exceeded, retrying later");
} catch (InvalidAuthorizationException e) {
    // Handle authentication failure - refresh token
    log.error("Authentication failed, refreshing token");
}
```

## Building from Source

Clone the repository and build with Maven:

```bash
git clone https://github.com/jottley/spring-salesforce.git
cd spring-salesforce
mvn clean install
```

Run tests:

```bash
mvn test
```

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

For bug reports and feature requests, please use the [GitHub Issues](https://github.com/jottley/spring-salesforce/issues) page.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Historical Notes

This project maintains part of the package naming (`org.springframework.salesforce`) for historical reasons related to its origins in the Spring Social project. While the package name includes "springframework", this is an independent project that leverages Spring Framework libraries and is intended for use in Spring-based applications. The legacy package naming should be considered deprecated and may be updated in a future major version.

## Support

- **Documentation**: This README and inline Javadocs
- **Example Application**: [Spring Salesforce Quickstart](https://github.com/jottley/spring-salesforce-quickstart)
- **Issues**: [GitHub Issues](https://github.com/jottley/spring-salesforce/issues)
- **Salesforce API Documentation**: [Salesforce REST API Developer Guide](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/)
