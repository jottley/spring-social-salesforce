# Connection Pool Configuration Proposal

## Executive Summary

Configure Apache HttpClient 5 connection pooling to optimize HTTP performance, prevent connection exhaustion, and set appropriate timeouts.

## Problem Statement

**Current State:**
- Uses default RestTemplate with auto-detected HTTP client
- No explicit connection pool configuration
- No timeout configuration (connect, read, write)
- May experience connection exhaustion under load
- No keep-alive strategy

**Impact:**
- Poor performance under concurrent load
- Connection leaks possible
- No control over timeouts
- Inefficient connection reuse

## Current State

**Dependencies:**
- Apache HttpClient 5.2.1 (pom.xml)
- RestTemplate with default request factory

**Files:**
- `SalesforceTemplate.java:106-129` - Basic RestTemplate setup
- `BaseSalesforceFactory.java:85-90` - Default configuration

## Proposed Solution

### Phase 1: HttpClient Configuration

```java
public class HttpClientConfig {
    private int maxConnections = 200;
    private int maxConnectionsPerRoute = 20;
    private int connectTimeout = 10000; // ms
    private int socketTimeout = 60000; // ms
    private int connectionRequestTimeout = 5000; // ms
    private long keepAliveTime = 20000; // ms
    private boolean enableCompression = true;
}
```

### Phase 2: Custom Request Factory

```java
protected ClientHttpRequestFactory createRequestFactory(HttpClientConfig config) {
    ConnectionConfig connectionConfig = ConnectionConfig.custom()
        .setSocketTimeout(Timeout.ofMilliseconds(config.getSocketTimeout()))
        .setConnectTimeout(Timeout.ofMilliseconds(config.getConnectTimeout()))
        .build();

    PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
        .setMaxConnTotal(config.getMaxConnections())
        .setMaxConnPerRoute(config.getMaxConnectionsPerRoute())
        .setDefaultConnectionConfig(connectionConfig)
        .build();

    HttpClient httpClient = HttpClients.custom()
        .setConnectionManager(cm)
        .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
        .setDefaultRequestConfig(RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(config.getConnectionRequestTimeout()))
            .build())
        .build();

    return new HttpComponentsClientHttpRequestFactory(httpClient);
}
```

### Phase 3: Configuration Options

**Via Constructor:**
```java
new SalesforceTemplate(accessToken, HttpClientConfig.builder()
    .maxConnections(100)
    .socketTimeout(30000)
    .build());
```

**Via Properties:**
```properties
salesforce.http.max-connections=200
salesforce.http.connect-timeout=10000
salesforce.http.socket-timeout=60000
```

### Phase 4: Connection Lifecycle

```java
// Add shutdown hook for connection manager cleanup
public void destroy() {
    if (connectionManager != null) {
        connectionManager.close();
    }
}
```

## Implementation Details

**New Files:**
- `client/HttpClientConfig.java`
- `client/HttpClientFactory.java`
- `client/SalesforceConnectionKeepAliveStrategy.java`

**Modified Files:**
- `SalesforceTemplate.java` - Use custom request factory
- `BaseSalesforceFactory.java` - Support HTTP config
- README.md - Document HTTP configuration

**Tests:**
- `HttpClientConfigTest.java`
- `ConnectionPoolIntegrationTest.java`
- Load testing scenarios

## Benefits

1. **Predictable performance** under load
2. **No connection exhaustion** - bounded pool
3. **Configurable timeouts** - prevent hung requests
4. **Connection reuse** - improved efficiency
5. **Production-ready** defaults
6. **Customizable** per deployment needs

## Timeline Estimate

- **Week 1**: HttpClient configuration + factory
- **Week 2**: Integration + lifecycle management
- **Week 3**: Testing + documentation
- **Total**: 3 weeks

## Default Configuration Recommendations

```java
// Development
maxConnections: 50
maxPerRoute: 10
connectTimeout: 10s
socketTimeout: 60s

// Production
maxConnections: 200
maxPerRoute: 20
connectTimeout: 5s
socketTimeout: 30s
keepAlive: 20s
```

## Open Questions

1. Should connection manager be shared across SalesforceTemplate instances?
2. Idle connection eviction strategy?
3. DNS caching configuration?
4. SSL/TLS configuration options?
5. Proxy support configuration?

## References

- [Apache HttpClient 5 Documentation](https://hc.apache.org/httpcomponents-client-5.2.x/)
- [Connection Management](https://hc.apache.org/httpcomponents-client-5.2.x/current/httpclient5/examples/org/apache/hc/client5/http/examples/ClientConfiguration.java)
- [Spring RestTemplate](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/client/RestTemplate.html)
