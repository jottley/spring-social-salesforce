package org.springframework.social.salesforce.api.impl;

import org.codehaus.jackson.JsonNode;
import org.springframework.social.salesforce.api.ResultItem;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.SearchOperations;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

/**
 * Default implementation of SearchOperations.
 *
 * @author Umut Utkan
 */
public class SearchTemplate extends AbstractSalesForceOperations<Salesforce> implements SearchOperations {

    private RestTemplate restTemplate;


    public SearchTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }


    @Override
    public List<ResultItem> search(String soslQuery) {
        requireAuthorization();
        URI uri = URIBuilder.fromUri(api.getBaseUrl() + "/v23.0/search").queryParam("q", soslQuery).build();
        JsonNode arr = restTemplate.getForObject(uri, JsonNode.class);
        return api.readList(arr, ResultItem.class);
    }

}
