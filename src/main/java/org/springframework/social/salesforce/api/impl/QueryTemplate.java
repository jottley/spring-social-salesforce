package org.springframework.social.salesforce.api.impl;

import org.springframework.social.salesforce.api.QueryOperations;
import org.springframework.social.salesforce.api.QueryResult;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Default implementation of QueryOperations.
 *
 * @author Umut Utkan
 */
public class QueryTemplate extends AbstractSalesForceOperations<Salesforce> implements QueryOperations {

    private RestTemplate restTemplate;


    public QueryTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }

    @Override
    public QueryResult query(String query) {
        requireAuthorization();
        URI uri = URIBuilder.fromUri(api.getBaseUrl() + "/v23.0/query").queryParam("q", query).build();
        return restTemplate.getForObject(uri, QueryResult.class);
    }

    @Override
    public QueryResult nextPage(String pathOrToken) {
        requireAuthorization();
        if (pathOrToken.contains("/")) {
            return restTemplate.getForObject(api.getBaseUrl() + pathOrToken, QueryResult.class);
        } else {
            return restTemplate.getForObject(api.getBaseUrl() + "/v23.0/query/{token}", QueryResult.class, pathOrToken);
        }
    }

}
