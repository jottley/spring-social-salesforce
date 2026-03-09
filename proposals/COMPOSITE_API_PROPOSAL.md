# Composite API Support Proposal

## Executive Summary
Implement Composite API for batching multiple operations in single request.

## Problem
- Multiple API calls for related operations
- No atomic transactions
- Higher API limit consumption

## Solution
```java
public interface CompositeOperations {
    CompositeResponse execute(CompositeRequest request);
    TreeResponse createTree(String rootType, Map<String, Object> tree);
    BatchResponse executeBatch(List<SubRequest> requests);
}
```

### Use Cases
- Create account + contacts in one call
- Update multiple unrelated records
- Mixed read/write operations

## Phases
1. Composite API (/composite)
2. Tree API (parent-child hierarchies)
3. Batch API (25 subrequests)
4. Graph API (complex relationships)

## Files
**New:** `CompositeOperations.java`, `CompositeTemplate.java`, models
**Modified:** `Salesforce.java`, `SalesforceTemplate.java`

## Timeline: 3 weeks

## References
- [Composite Resources](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/resources_composite.htm)
