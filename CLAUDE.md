# Spring Salesforce - Claude Development Guide

## Project Overview

Spring Salesforce is a Spring Framework extension that provides OAuth2 connection support and comprehensive API bindings for the Salesforce REST API. This library simplifies Salesforce integration by offering a Spring-native interface for querying, creating, updating, and managing Salesforce data.

**Key Features:**
- OAuth2 authentication (with or without Spring Security)
- Complete CRUD operations on sObjects
- SOQL query and SOSL search support
- Chatter/feed operations
- Custom Apex REST API integration
- Communities (Digital Experience) support
- API version management with deprecation handling

## Technology Stack

- **Java**: 17+ (target/source)
- **Spring Framework**: 7.0.0
- **Spring Security**: 7.0.0 (optional)
- **Jackson**: 2.18.5 (2.x branch, not 3.x)
- **Build Tool**: Maven 3.6+
- **Testing**: JUnit 4.13.1, Mockito 2.11.0
- **Salesforce API**: v66.0 (default), v31.0+ (minimum supported)

## Project Structure

```
spring-salesforce/
├── src/main/java/org/springframework/salesforce/
│   ├── api/                          # Public API interfaces and exceptions
│   │   ├── ApiOperations.java        # API version and service discovery
│   │   ├── SObjectOperations.java    # sObject CRUD operations
│   │   ├── QueryOperations.java      # SOQL query operations
│   │   ├── CustomApiOperations.java  # Custom Apex REST APIs
│   │   └── [Other operation interfaces]
│   ├── api/impl/                     # Implementation classes
│   │   ├── SalesforceTemplate.java   # Main entry point, factory for operations
│   │   ├── ApiTemplate.java          # API version management
│   │   ├── SalesforceErrorHandler.java # HTTP error handling (410 GONE, 401, etc.)
│   │   └── [Template implementations]
│   └── client/
│       └── ErrorHandler.java         # OAuth2 error handler (BAD_REQUEST)
├── src/test/java/                    # Unit and integration tests
├── src/test/resources/               # JSON mock responses (v66.0)
├── pom.xml                           # Maven build configuration
├── README.md                         # User-facing documentation
└── proposals/                        # Future enhancement proposals
```

## Architecture Patterns

### 1. Operations Interface Pattern

All Salesforce operations follow a consistent interface/implementation pattern:

```java
// Public interface
public interface SObjectOperations {
    Map<String, Object> getRow(String type, String id);
    String create(String type, Map<String, Object> fields);
    // ...
}

// Implementation extends base class
public class SObjectsTemplate extends AbstractSalesForceOperations implements SObjectOperations {
    // Uses RestTemplate for HTTP calls
    // Accesses version via getVersion()
}
```

**API Documentation:**
- [sObject Rows](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/dome_sobject_retrieve.htm)
- [Query Resource](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_query.htm)
- [Search Resource](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_search.htm)

### 2. Template/Factory Pattern

`SalesforceTemplate` acts as the central factory:
- Creates all operation instances
- Manages shared RestTemplate
- Provides instance URL configuration
- Coordinates API version settings

### 3. URL Construction Pattern

All endpoints follow: `{instanceUrl}/services/data/{version}/{resource}`

Example: `https://na1.salesforce.com/services/data/v66.0/sobjects/Account`

**Exception**: Custom Apex APIs use: `{instanceUrl}/services/apexrest/{path}` (no version)

**Salesforce Documentation:**
- [REST API Resources Overview](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_list.htm)
- [Custom REST API in Apex](https://developer.salesforce.com/docs/atlas.en-us.apexcode.meta/apexcode/apex_rest.htm)

### 4. Error Handling Strategy

Two error handlers work in tandem:
- **ErrorHandler**: OAuth2 errors (400 BAD_REQUEST)
- **SalesforceErrorHandler**: API errors (401, 403, 404, 410, 500, 503)

Modern switch expressions with arrow syntax are preferred for error routing.

**Salesforce Documentation:**
- [Status Codes and Error Responses](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/errorcodes.htm)
- [OAuth Error Codes](https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_error_codes.htm)

## API Version Management

**Current State:**
- DEFAULT_API_VERSION: `v66.0`
- MINIMUM_API_VERSION: `v31.0`
- Version validation enforced at runtime
- HTTP 410 GONE triggers `DeprecatedApiVersionException`

**Version Update Process:**
1. Update constants in `ApiOperations.java`
2. Update test resources (19+ JSON files with URLs)
3. Run full test suite (48 tests)
4. Update README.md with new version

**Salesforce API Version Documentation:**
- [API Versions](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_versions.htm)
- [API End-of-Life Policy](https://help.salesforce.com/s/articleView?id=000389618)
- [Version Release Notes](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_release_notes.htm)

**Future Enhancement:**
See [API_VERSION_MANAGEMENT_PROPOSAL.md](proposals/API_VERSION_MANAGEMENT_PROPOSAL.md) for planned improvements including:
- Endpoint-level version requirements via annotations
- Feature detection and graceful degradation
- Automated version compatibility checks

## Code Conventions

### Java Style

1. **Tabs for indentation** (not spaces)
2. **Opening brace on same line** for methods and classes
3. **Author attribution**: `@author Jared Ottley` (capitalize "Ottley")
4. **Modern Java features**: Use switch expressions, records where appropriate
5. **Non-null annotations**: Use `@NonNull` from jspecify

### Switch Statements

**Prefer modern switch expressions** (Java 17+):

```java
// ✅ Good - Modern switch expression
switch (errorCode) {
    case "invalid_client_id", "invalid_scope" ->
        throw new InvalidAuthorizationException(description);
    case "rate_limit_exceeded" ->
        throw new RateLimitExceededException("Rate limit exceeded");
    default ->
        throw new UncategorizedApiException(message);
}
```

**Avoid** traditional switch with fall-through:
```java
// ❌ Avoid - Traditional switch
switch (errorCode) {
    case "invalid_client_id":
    case "invalid_scope":
        throw new InvalidAuthorizationException(description);
        break;
    // ...
}
```

### Exception Handling

- Extend `ApiException` for Salesforce-specific errors
- Include contextual information in exception messages
- Reference Salesforce help URLs when applicable
- Use specific exception types (don't overuse `UncategorizedApiException`)

### Test Conventions

- Use MockRestServiceServer for HTTP mocking
- Test resources in JSON format with v66.0 URLs
- 48 tests total - all must pass
- Test file naming: `*Test.java` or `*TemplateTest.java`

## Common Tasks

### Running Tests

```bash
mvn test
```

All 48 tests must pass. Test execution time: ~2-3 seconds.

### Building the Project

```bash
mvn clean install
```

### Updating API Version

1. Update `ApiOperations.java`:
   ```java
   String DEFAULT_API_VERSION = "v67.0";  // Update this
   ```

2. Update test resources (search for old version):
   ```bash
   grep -r "v66.0" src/test/resources/
   ```

3. Run tests to verify:
   ```bash
   mvn test
   ```

### Adding New Operations

1. Define interface in `src/main/java/.../api/`
2. Create implementation in `src/main/java/.../api/impl/`
3. Extend `AbstractSalesForceOperations`
4. Add to `SalesforceTemplate.initialize()`
5. Add interface method to `Salesforce.java`
6. Write tests with mock responses

## Important Notes

### Deprecation Warnings

**MappingJackson2HttpMessageConverter Deprecation:**
- Currently using Jackson 2.x (not 3.x)
- Spring 7.0 deprecated `MappingJackson2HttpMessageConverter`
- Replacement requires Jackson 3.x migration (breaking change)
- Suppressed with `@SuppressWarnings("removal")`
- TODO documented in `SalesforceTemplate.getJsonMessageConverter()`
- Plan to address in future major version (3.0.0)

### Spelling and Naming

**Recent Fixes:**
- ✅ Fixed: "customApiOperatinos" → "customApiOperations" (typo)
- ✅ Fixed: "Bad_OAuth_Toekn" → "Bad_OAuth_Token" (comment typo)
- ✅ Fixed: ErrorHandler switch statement (if-else → switch)

**Watch for:**
- Consistent capitalization of "Ottley" in @author tags
- Salesforce (not "Salesforces")
- Proper possessive: "user's" not "users's"

### Thread Safety

- `ApiTemplate` stores version as instance field
- Thread safety not explicitly documented
- Assume single-threaded usage per instance

### Custom Apex APIs

Custom Apex REST endpoints differ from standard API:
- Path: `/services/apexrest{uriPath}` (NO `/data/{version}`)
- No version number in URL
- Use `CustomApiOperations` interface
- Supports GET, POST, PUT, PATCH, DELETE

## Testing Strategy

### Mock Response Pattern

1. Create JSON file in `src/test/resources/` (e.g., `account.json`)
2. Set up MockRestServiceServer in test:
   ```java
   mockServer.expect(requestTo(url))
       .andExpect(method(HttpMethod.GET))
       .andRespond(withSuccess(new ClassPathResource("account.json"),
                   MediaType.APPLICATION_JSON));
   ```
3. Execute operation and verify results

### Test Resource Maintenance

- JSON files contain hardcoded `v66.0` in URLs
- Must update manually when version changes
- Future enhancement: template-based approach (see proposal)

## Recent Changes

**Session Work (March 9, 2026):**
1. ✅ Converted ErrorHandler if-else chain to switch expression
2. ✅ Added default case for unrecognized OAuth errors
3. ✅ Fixed "customApiOperatinos" typo throughout codebase
4. ✅ Enhanced README.md with comprehensive documentation
5. ✅ Fixed "Bad_OAuth_Token" typo in comment
6. ✅ Documented Jackson 2.x deprecation strategy
7. ✅ Created API version management proposal

**Recent Commits:**
- 7e2b5b2: Upgrade to API version v66.0
- e679607: Enhanced API version validation and deprecation handling
- Added `DeprecatedApiVersionException`
- Added `ApiVersionValidationTest`

## Dependencies to Watch

**Keep Updated:**
- Spring Framework 7.0.x
- Jackson 2.x (latest 2.x, not 3.x yet)
- Logback 1.5.x
- Commons Lang3 3.20+

**Testing Dependencies:**
- JUnit 4.x (not migrated to JUnit 5 yet)
- Mockito 2.x

## Build Configuration

### Maven Properties

```xml
<jdk.version>17</jdk.version>
<spring.version>7.0.0</spring.version>
<jackson.version>2.18.5</jackson.version>
```

### Distribution Repository

```
https://repo.repsy.io/mvn/jottley/spring-salesforce
```

## Documentation Standards

### README.md

- Comprehensive installation instructions
- Quick start code examples
- Complete operations list with descriptions
- Error handling guide
- Building from source instructions
- Badge section (build status, license, Java version)

### Javadoc

- All public interfaces documented
- Include `@author` tags
- Document exceptions with `@throws`
- Include usage examples in class-level docs

### Code Comments

- Explain "why" not "what"
- Document non-obvious behavior
- Include Salesforce API references when relevant
- Add TODOs for future enhancements with context

## Git Workflow

**Branch Strategy:**
- `master` - main branch (production-ready)
- Feature branches for development
- All tests must pass before merge

**Commit Messages:**
- Use imperative mood: "Add feature" not "Added feature"
- Reference issues when applicable
- Include co-author when AI-assisted:
  ```
  Co-Authored-By: Claude Sonnet 4.5 <noreply@anthropic.com>
  ```

## Continuous Integration

**GitHub Actions:**
- Workflow: `.github/workflows/maven.yml`
- Runs on: push to master, pull requests
- Java version: 17 (Temurin distribution)
- Maven command: `mvn -B package --file pom.xml`
- Dependency graph submission enabled

## Future Enhancements

See [proposals/](proposals/) directory for detailed enhancement proposals:

**High Priority:**
- [API Version Management](proposals/API_VERSION_MANAGEMENT_PROPOSAL.md) - Endpoint-level version requirements and feature detection
- [Automatic Retry Logic](proposals/RETRY_LOGIC_PROPOSAL.md) - Exponential backoff for transient failures
- [Connection Pooling](proposals/CONNECTION_POOL_PROPOSAL.md) - HttpClient configuration optimization

**Medium Priority:**
- [Bulk API Support](proposals/BULK_API_PROPOSAL.md) - Bulk API v2.0 for large-scale operations
- [Composite API](proposals/COMPOSITE_API_PROPOSAL.md) - Batch multiple operations in single request
- [Query Memory Management](proposals/QUERY_MEMORY_PROPOSAL.md) - Streaming pagination for large result sets
- [Proactive Throttling](proposals/THROTTLING_PROPOSAL.md) - Prevent API limit exhaustion

**Quick Wins:**
- [HTTP Compression](proposals/COMPRESSION_PROPOSAL.md) - Enable gzip for bandwidth reduction
- [Enhanced Caching](proposals/CACHING_PROPOSAL.md) - Multi-level caching strategy

**Other Potential Enhancements:**
- Migration to JUnit 5
- Upgrade to Jackson 3.x (requires major version bump)
- Spring Boot 3.x support/testing
- Enhanced logging with structured output
- Metrics/observability integration

## Salesforce API References

### Core Documentation
- **[REST API Developer Guide](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/)** - Complete REST API reference
- **[API Reference](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_list.htm)** - All available REST resources
- **[API Versioning](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_versions.htm)** - Version management and lifecycle
- **[API Limits](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/limits.htm)** - Rate limits and allocations

### Authentication & Authorization
- **[OAuth 2.0](https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_flows.htm)** - OAuth flows and implementation
- **[Connected Apps](https://help.salesforce.com/s/articleView?id=sf.connected_app_overview.htm)** - Setting up OAuth apps
- **[OAuth Error Codes](https://help.salesforce.com/s/articleView?id=sf.remoteaccess_oauth_error_codes.htm)** - Authentication error reference

### Data Operations
- **[sObjects](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/dome_sobject_basic_info.htm)** - sObject resource documentation
- **[SOQL](https://developer.salesforce.com/docs/atlas.en-us.soql_sosl.meta/soql_sosl/sforce_api_calls_soql.htm)** - Salesforce Object Query Language
- **[SOSL](https://developer.salesforce.com/docs/atlas.en-us.soql_sosl.meta/soql_sosl/sforce_api_calls_sosl.htm)** - Salesforce Object Search Language
- **[Composite Resources](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_composite.htm)** - Batch operations

### Feature-Specific Documentation
- **[Chatter REST API](https://developer.salesforce.com/docs/atlas.en-us.chatterapi.meta/chatterapi/)** - Social feed operations
- **[Communities (Experience Cloud)](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_connect_communities.htm)** - Community management
- **[Custom Apex REST](https://developer.salesforce.com/docs/atlas.en-us.apexcode.meta/apexcode/apex_rest.htm)** - Custom endpoint creation
- **[Limits](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_limits.htm)** - Organization limits API

### Error Handling
- **[Status Codes](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/errorcodes.htm)** - HTTP status codes and meanings
- **[Error Response Format](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_rest_api_examples.htm)** - Error message structure

### Developer Resources
- **[API Release Notes](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_release_notes.htm)** - Version-specific changes
- **[API End-of-Life](https://help.salesforce.com/s/articleView?id=000389618)** - Version deprecation policy
- **[Developer Console](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_curl.htm)** - Testing with cURL

## Getting Help

- **Example Project**: https://github.com/jottley/spring-salesforce-quickstart
- **Issues**: https://github.com/jottley/spring-salesforce/issues
- **Salesforce Developer Forums**: https://developer.salesforce.com/forums

## Project Maintainer

**Jared Ottley**
- GitHub: @jottley
- Project URL: https://github.com/jottley/spring-salesforce

---

**License**: Apache License 2.0
