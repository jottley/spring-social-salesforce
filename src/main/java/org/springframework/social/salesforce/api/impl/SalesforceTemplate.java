package org.springframework.social.salesforce.api.impl;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.UncategorizedApiException;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.salesforce.api.*;
import org.springframework.social.salesforce.api.impl.json.SalesforceModule;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Default implementation of Salesforce. This is the main entry point for all
 * the operations that can be performed on Salesforce.
 * 
 * @author Umut Utkan
 */
public class SalesforceTemplate extends AbstractOAuth2ApiBinding implements Salesforce {
    private final Logger logger = LoggerFactory.getLogger(SalesforceTemplate.class);

    private static final String INSTANCE_URL = "https://na1.salesforce.com";

    private String instanceUrl;

    private ObjectMapper objectMapper;

    private ApiOperations apiOperations;

    private ChatterOperations chatterOperations;

    private QueryOperations queryOperations;

    private RecentOperations recentOperations;

    private SearchOperations searchOperations;

    private SObjectOperations sObjectsOperations;

    public SalesforceTemplate() {
        initialize();
    }

    public SalesforceTemplate(String accessToken) {
        super(accessToken);
        initialize();
        logger.debug("ACCESS TOKEN: {}", accessToken);
    }

    @Override
    protected OAuth2Version getOAuth2Version() {
        return OAuth2Version.DRAFT_10;
    }

    @Override
    public ApiOperations apiOperations() {
        return apiOperations;
    }

    @Override
    public ChatterOperations chatterOperations() {
        return chatterOperations;
    }

    @Override
    public QueryOperations queryOperations() {
        return queryOperations;
    }

    @Override
    public RecentOperations recentOperations() {
        return recentOperations;
    }

    @Override
    public SearchOperations searchOperations() {
        return searchOperations;
    }

    @Override
    public SObjectOperations sObjectsOperations() {
        return sObjectsOperations;
    }

    private void initialize() {
        apiOperations = new ApiTemplate(this, getRestTemplate());
        chatterOperations = new ChatterTemplate(this, getRestTemplate());
        queryOperations = new QueryTemplate(this, getRestTemplate());
        recentOperations = new RecentTemplate(this, getRestTemplate());
        searchOperations = new SearchTemplate(this, getRestTemplate());
        sObjectsOperations = new SObjectsTemplate(this, getRestTemplate());
    }

    @Override
    protected MappingJacksonHttpMessageConverter getJsonMessageConverter() {
        MappingJacksonHttpMessageConverter converter = super.getJsonMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SalesforceModule());
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    @Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new SalesforceErrorHandler());
    }

    @Override
    public <T> List<T> readList(JsonNode jsonNode, Class<T> type) {
        try {
            CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, type);
            return (List<T>) objectMapper.readValue(jsonNode, listType);
        } catch (IOException e) {
            throw new UncategorizedApiException("Error deserializing data from Salesforce: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> T readObject(JsonNode jsonNode, Class<T> type) {
        try {
            return (T) objectMapper.readValue(jsonNode, type);
        } catch (IOException e) {
            throw new UncategorizedApiException("Error deserializing data from Salesforce: " + e.getMessage(), e);
        }
    }

    @Override
    public String getBaseUrl() {
        return (this.instanceUrl == null ? INSTANCE_URL : this.instanceUrl) + "/services/data";
    }

    @Override
    public String getInstanceUrl() {
        return this.instanceUrl;
    }

    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }

}
