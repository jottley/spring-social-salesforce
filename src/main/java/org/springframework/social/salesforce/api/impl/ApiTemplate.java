package org.springframework.social.salesforce.api.impl;

import org.codehaus.jackson.JsonNode;
import org.springframework.social.salesforce.api.ApiOperations;
import org.springframework.social.salesforce.api.ApiVersion;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of ApiOperations.
 *
 * @author Umut Utkan
 */
public class ApiTemplate extends AbstractSalesForceOperations<Salesforce> implements ApiOperations {

    private RestTemplate restTemplate;


    public ApiTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }


    public List<ApiVersion> getVersions() {
        URI uri = URIBuilder.fromUri(api.getBaseUrl()).build();
        JsonNode dataNode = restTemplate.getForObject(uri, JsonNode.class);
        return api.readList(dataNode, ApiVersion.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getServices(String version) {
        requireAuthorization();
        return restTemplate.getForObject(api.getBaseUrl() + "/v{version}", Map.class, version);
    }

}
