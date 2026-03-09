# API Version Management Enhancement Proposal

## Executive Summary

This document proposes enhancements to spring-salesforce's API version management to handle Salesforce's regular API releases, deprecations, and version-specific features.

## Problem Statement

Salesforce releases new REST API versions three times per year. Each release can:
- **Deprecate endpoints** - Older versions return HTTP 410 GONE after end-of-life
- **Add new features** - Some endpoints only available in newer versions
- **Change behavior** - Same endpoint may work differently across versions

The library currently has basic version management but lacks:
- Endpoint-level version requirements
- Deprecation warnings for "deprecated but working" versions
- Version-specific feature detection
- Automatic version negotiation

## Current State

**Existing Capabilities:**
- DEFAULT_API_VERSION: v66.0
- MINIMUM_API_VERSION: v31.0
- Version validation (format + minimum threshold)
- HTTP 410 GONE error handling
- Parameterized URL construction

**Gaps:**
- No per-operation version requirements
- Binary valid/invalid (no warnings)
- Manual constant updates required
- No feature compatibility matrix
- No conditional code paths by version

## Proposed Solution

### Phase 1: Endpoint Version Metadata

**1.1 Version Requirement Annotation**
```java
@RequiresApiVersion("v45.0")
List<Community> getCommunities();
```

**1.2 Deprecation Warning Annotation**
```java
@DeprecatedInApiVersion(value = "v50.0", removedIn = "v55.0")
void legacyOperation();
```

**1.3 Runtime Version Checker**
- Validates current version meets requirements
- Throws `InsufficientApiVersionException` when needed
- Logs warnings for deprecated operations

### Phase 2: Enhanced Version Information

**2.1 Expand ApiVersion Model**
Add lifecycle metadata:
- deprecatedDate
- endOfLifeDate
- releaseDate
- supportsFeatures

**2.2 Version Registry**
Static registry mapping versions to capabilities:
```java
ApiVersionRegistry.supportsFeature("external-objects", "v32.0")
```

**2.3 Version Discovery**
Cache and cross-reference available versions from Salesforce API

### Phase 3: Graceful Degradation

**3.1 Fallback Strategies**
```java
enum VersionFallbackStrategy {
    FAIL_FAST,
    USE_DEFAULT,
    USE_HIGHEST_COMPATIBLE
}
```

**3.2 Feature Detection**
```java
boolean supportsChatterBanner();
boolean supportsExternalObjects();
```

**3.3 Conditional Execution**
```java
if (apiOperations().getVersionAsDouble() >= 45.0) {
    // Use newer approach
}
```

### Phase 4: Developer Experience

**4.1 Version Compatibility Matrix**
Document feature availability by version in README

**4.2 Enhanced Logging**
- Deprecation warnings
- Feature unavailability notices
- Version negotiation decisions

**4.3 External Configuration**
```properties
salesforce.api.version=v66.0
```

### Phase 5: Maintenance Automation

**5.1 Properties-Based Configuration**
```properties
default.version=v66.0
minimum.version=v31.0
```

**5.2 Test Resource Templating**
```json
"url": "/services/data/${api.version}/sobjects"
```

**5.3 CI/CD Version Checks**
Automated detection of new Salesforce releases

## Implementation Priority

**High Priority:**
1. RequiresApiVersion annotation
2. ApiVersionChecker utility
3. Version logging
4. README documentation

**Medium Priority:**
5. Version registry
6. Fallback strategies
7. External configuration

**Low Priority:**
8. Test templating
9. CI/CD integration
10. Enhanced metadata

## Files to Create

- `RequiresApiVersion.java` - Annotation for minimum version
- `DeprecatedInApiVersion.java` - Deprecation metadata
- `InsufficientApiVersionException.java` - New exception type
- `ApiVersionChecker.java` - Runtime validation
- `ApiVersionRegistry.java` - Feature mapping
- `salesforce-versions.properties` - Version config

## Files to Modify

- `ApiOperations.java` - Feature detection methods
- `ApiTemplate.java` - Integrate checking
- `ApiVersion.java` - Lifecycle fields
- `README.md` - Version guide

## Benefits

1. **Prevents runtime errors** - Early version mismatch detection
2. **Clear documentation** - Visible version requirements
3. **Graceful degradation** - Handle version differences
4. **Easier maintenance** - Centralized management
5. **Better UX** - Clear error messages
6. **Future-proof** - Easy to extend

## Example Usage

```java
// Version configuration
salesforce.apiOperations().setVersion("v45.0");

// Feature detection
if (salesforce.apiOperations().supportsFeature("chatter-banner")) {
    // Use feature
}

// Annotated interface
public interface ChatterOperations {
    @RequiresApiVersion(value = "v50.0", reason = "Banner API added in v50.0")
    void updateBanner(String userId, String imageUrl);
}

// Runtime validation
try {
    chatterOps.updateBanner(userId, url);
} catch (InsufficientApiVersionException e) {
    log.warn("Feature not available: {}", e.getMessage());
}
```

## Migration Path

1. Add annotations to existing operations
2. Enable logging (non-breaking)
3. Deploy version checker (warnings only)
4. Enforce requirements (breaking change - major version)

## Testing Strategy

- Unit tests for version comparison logic
- Integration tests with multiple versions
- Annotation processing tests
- Fallback strategy tests
- Documentation examples verification

## Timeline Estimate

- **Phase 1**: 1-2 weeks (annotations + checker)
- **Phase 2**: 1 week (registry + metadata)
- **Phase 3**: 1 week (fallback logic)
- **Phase 4**: 1 week (docs + logging)
- **Phase 5**: 2 weeks (automation)

**Total**: 6-7 weeks for complete implementation

## Open Questions

1. Should version checking be opt-in or opt-out?
2. How verbose should deprecation warnings be?
3. Should fallback be automatic or require explicit config?
4. How to handle custom Apex APIs (currently unversioned)?
5. Should we support version ranges (e.g., "v45.0-v50.0")?

## References

- [Salesforce API Versions](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/intro_versions.htm)
- [API End of Life Policy](https://help.salesforce.com/s/articleView?id=000389618)
- Current implementation: `ApiTemplate.java`, `ApiOperations.java`
- Recent changes: Commit 7e2b5b2 (v66.0 upgrade), e679607 (deprecation handling)
