# Retry Logic Enhancement Proposal

## Executive Summary

Implement automatic retry with exponential backoff for transient failures and rate limiting to improve reliability and user experience.

## Problem Statement

**Current Behavior:**
- Detects rate limits (HTTP 503) and throws `RateLimitExceededException`
- No automatic retry mechanism
- Users must implement their own retry logic
- Transient network failures cause immediate failure

**Impact:**
- Poor user experience during rate limit scenarios
- Unnecessary application crashes on transient errors
- Duplicated retry logic across applications

## Current State

**Files:**
- `SalesforceErrorHandler.java:83-85` - Detects 503, throws exception
- `RateLimitExceededException.java` - Exception class
- README.md:161-167 - Documents manual retry approach

**What Works:**
- Exception detection and classification
- Proper exception hierarchy

## Proposed Solution

### Phase 1: Retry Interceptor (Foundation)

Create `SalesforceRetryInterceptor` implementing `ClientHttpRequestInterceptor`:

```java
public class SalesforceRetryInterceptor implements ClientHttpRequestInterceptor {
    private final RetryPolicy retryPolicy;
    private final BackoffStrategy backoffStrategy;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                       ClientHttpRequestExecution execution) {
        return executeWithRetry(request, body, execution);
    }
}
```

### Phase 2: Retry Configuration

```java
public class RetryConfig {
    private int maxAttempts = 3;
    private long initialDelay = 1000; // ms
    private double multiplier = 2.0;
    private long maxDelay = 30000; // ms
    private Set<HttpStatus> retryableStatuses;
    private boolean retryOn5xx = true;
}
```

### Phase 3: Backoff Strategies

```java
public interface BackoffStrategy {
    long calculateDelay(int attemptNumber, long initialDelay);
}

// Exponential: delay = initialDelay * (multiplier ^ attempt)
// Exponential with jitter: adds randomness to prevent thundering herd
```

### Phase 4: Integration

Add to `SalesforceTemplate.initializeRestTemplate()`:
```java
if (retryConfig != null && retryConfig.isEnabled()) {
    interceptors.add(new SalesforceRetryInterceptor(retryConfig));
}
```

## Implementation Details

**New Files:**
- `api/retry/RetryPolicy.java`
- `api/retry/RetryConfig.java`
- `api/retry/BackoffStrategy.java`
- `api/retry/ExponentialBackoff.java`
- `api/retry/ExponentialJitterBackoff.java`
- `impl/SalesforceRetryInterceptor.java`

**Modified Files:**
- `SalesforceTemplate.java` - Add retry configuration
- `BaseSalesforceFactory.java` - Support retry config
- README.md - Document retry configuration

**Tests:**
- `SalesforceRetryInterceptorTest.java`
- Integration tests with MockRestServiceServer

## Benefits

1. **Automatic recovery** from transient failures
2. **Better rate limit handling** - respects Salesforce throttling
3. **Reduced application errors** - fewer crashes
4. **Configurable** - users control retry behavior
5. **Jitter support** - prevents thundering herd
6. **Observable** - log retry attempts

## Timeline Estimate

- **Week 1**: Core retry interceptor + exponential backoff
- **Week 2**: Configuration + integration
- **Week 3**: Testing + documentation
- **Total**: 3 weeks

## Open Questions

1. Should retry be opt-in or opt-out?
2. Default max attempts (3? 5?)?
3. Should we parse `Retry-After` header from 503 responses?
4. Circuit breaker integration?
5. Metrics/observability hooks?

## References

- [Spring Retry](https://github.com/spring-projects/spring-retry)
- [Salesforce Rate Limiting](https://developer.salesforce.com/docs/atlas.en-us.api_rest.meta/api_rest/limits.htm)
- [AWS Exponential Backoff](https://aws.amazon.com/blogs/architecture/exponential-backoff-and-jitter/)
