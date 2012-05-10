package org.springframework.social.salesforce.api.impl;

import org.codehaus.jackson.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.social.salesforce.api.SObjectDetail;
import org.springframework.social.salesforce.api.SObjectOperations;
import org.springframework.social.salesforce.api.SObjectSummary;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.support.URIBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of SObjectOperations.
 *
 * @author Umut Utkan
 */
public class SObjectsTemplate extends AbstractSalesForceOperations<Salesforce> implements SObjectOperations {

    private RestTemplate restTemplate;

    public SObjectsTemplate(Salesforce api, RestTemplate restTemplate) {
        super(api);
        this.restTemplate = restTemplate;
    }


    @Override
    public List<Map> getSObjects() {
        requireAuthorization();
        JsonNode dataNode = restTemplate.getForObject(api.getBaseUrl() + "/v23.0/sobjects", JsonNode.class);
        return api.readList(dataNode.get("sobjects"), Map.class);
    }

    @Override
    public SObjectSummary getSObject(String name) {
        requireAuthorization();
        JsonNode node = restTemplate.getForObject(api.getBaseUrl() + "/v23.0/sobjects/{name}", JsonNode.class, name);
        return api.readObject(node.get("objectDescribe"), SObjectSummary.class);
    }

    @Override
    public SObjectDetail describeSObject(String name) {
        requireAuthorization();
        return restTemplate.getForObject(api.getBaseUrl() + "/v23.0/sobjects/{name}/describe", SObjectDetail.class, name);
    }

    @Override
    public Map getRow(String name, String id, String... fields) {
        requireAuthorization();
        URIBuilder builder = URIBuilder.fromUri(api.getBaseUrl() + "/v23.0/sobjects/" + name + "/" + id);
        if (fields.length > 0) {
            builder.queryParam("fields", StringUtils.arrayToCommaDelimitedString(fields));
        }
        return restTemplate.getForObject(builder.build(), Map.class);
    }

    @Override
    public InputStream getBlob(String name, String id, String field) {
        requireAuthorization();
        return restTemplate.execute(api.getBaseUrl() + "/v23.0/sobjects/{name}/{id}/{field}",
                HttpMethod.GET, null, new ResponseExtractor<InputStream>() {
            @Override
            public InputStream extractData(ClientHttpResponse response) throws IOException {
                return response.getBody();
            }
        }, name, id, field);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Map<?, ?> create(String name, Map<String, String> fields) {
        requireAuthorization();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map> entity = new HttpEntity<Map>(fields, headers);
        return restTemplate.postForObject(api.getBaseUrl() + "/v23.0/sobjects/{name}", entity, Map.class, name);
    }

}
