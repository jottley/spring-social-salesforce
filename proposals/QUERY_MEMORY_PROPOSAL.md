# Query Memory Management Proposal

## Executive Summary
Add streaming pagination to prevent OutOfMemoryError on large query results.

## Problem
- `queryAll()` loads entire result set into memory
- Risk of OOM on 100,000+ record queries
- No streaming alternative

## Solution
### Streaming Query Iterator
```java
public interface StreamingQueryOperations {
    Iterator<Map<String, Object>> queryIterator(String soql);
    Stream<Map<String, Object>> queryStream(String soql);
    void queryWithConsumer(String soql, Consumer<Map<String, Object>> consumer);
}
```

### Implementation
```java
Iterator<Map<String, Object>> iter = salesforce.streamingQueryOperations()
    .queryIterator("SELECT Id, Name FROM Account");

while (iter.hasNext()) {
    Map<String, Object> record = iter.next();
    // Process one record at a time
}
```

## Phases
1. Lazy-loading iterator with automatic pagination
2. Java Stream API support
3. Consumer-based processing
4. Configurable batch size

## Files
**New:** `StreamingQueryOperations.java`, `StreamingQueryTemplate.java`, `LazyQueryIterator.java`
**Modified:** `Salesforce.java`, `QueryTemplate.java`

## Benefits
- Constant memory usage regardless of result size
- Process millions of records safely
- Backpressure support

## Timeline: 2 weeks

## References
- [SOQL Query Best Practices](https://developer.salesforce.com/docs/atlas.en-us.soql_sosl.meta/soql_sosl/sforce_api_calls_soql_best_practices.htm)
