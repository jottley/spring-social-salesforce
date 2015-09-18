package org.springframework.social.salesforce.api.impl;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Interceptor for adding headers
 *
 * @author Umut Utkan
 */
public class HeaderAddingInterceptor implements ClientHttpRequestInterceptor {

    private Map<String, String> headers;

    public HeaderAddingInterceptor(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes,
                                        ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        for (Map.Entry<String, String> header : this.headers.entrySet()) {
            httpRequest.getHeaders().add(header.getKey(), header.getValue());
        }
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

}
