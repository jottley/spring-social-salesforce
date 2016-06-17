package org.springframework.social.salesforce.api.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.salesforce.api.ApiOperations;
import org.springframework.social.salesforce.api.ChatterOperations;
import org.springframework.social.salesforce.api.QueryOperations;
import org.springframework.social.salesforce.api.RecentOperations;
import org.springframework.social.salesforce.api.SObjectOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.SearchOperations;
import org.springframework.social.salesforce.api.SalesforceUserDetails;
import org.springframework.social.salesforce.api.UserOperations;
import org.springframework.social.salesforce.api.impl.json.SalesforceModule;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of Salesforce. This is the main entry point for all
 * the operations that can be performed on Salesforce.
 * 
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class SalesforceTemplate extends AbstractOAuth2ApiBinding implements Salesforce
{
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

    private UserOperations userOperations;


    public SalesforceTemplate()
    {
        initialize();
    }


    public SalesforceTemplate(String accessToken)
    {
        super(accessToken);
        initialize();
        logger.debug("ACCESS TOKEN: {}", accessToken);
    }


    @Override
    protected OAuth2Version getOAuth2Version()
    {
        return OAuth2Version.BEARER;
    }


    @Override
    public ApiOperations apiOperations()
    {
        return apiOperations;
    }


    @Override
    public ApiOperations apiOperations(String instanceUrl)
    {
        this.instanceUrl = instanceUrl;

        return apiOperations;
    }


    @Override
    public ChatterOperations chatterOperations()
    {
        return chatterOperations;
    }


    @Override
    public ChatterOperations chatterOperations(String instanceUrl)
    {
        this.instanceUrl = instanceUrl;
        return chatterOperations;
    }


    @Override
    public QueryOperations queryOperations()
    {
        return queryOperations;
    }


    @Override
    public QueryOperations queryOperations(String instanceUrl)
    {
        this.instanceUrl = instanceUrl;
        return queryOperations;
    }


    @Override
    public RecentOperations recentOperations()
    {
        return recentOperations;
    }


    @Override
    public RecentOperations recentOperations(String instanceUrl)
    {
        this.instanceUrl = instanceUrl;
        return recentOperations;
    }


    @Override
    public SearchOperations searchOperations()
    {
        return searchOperations;
    }


    @Override
    public SearchOperations searchOperations(String instanceUrl)
    {
        this.instanceUrl = instanceUrl;

        return searchOperations;
    }


    @Override
    public SObjectOperations sObjectsOperations()
    {
        return sObjectsOperations;
    }


    @Override
    public SObjectOperations sObjectsOperations(String instanceUrl)
    {
        this.instanceUrl = instanceUrl;
        return sObjectsOperations;
    }

    @Override
    public UserOperations userOperations() 
    {
        return userOperations;
    }


    private void initialize()
    {
        apiOperations = new ApiTemplate(this, getRestTemplate());
        chatterOperations = new ChatterTemplate(this, getRestTemplate());
        queryOperations = new QueryTemplate(this, getRestTemplate());
        recentOperations = new RecentTemplate(this, getRestTemplate());
        searchOperations = new SearchTemplate(this, getRestTemplate());
        sObjectsOperations = new SObjectsTemplate(this, getRestTemplate());
        userOperations = new UserOperationsTemplate(this, getRestTemplate());
    }


    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters()
    {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(getFormMessageConverter());
        messageConverters.add(this.getJsonMessageConverter2());
        messageConverters.add(getByteArrayMessageConverter());
        return messageConverters;
    }

    protected MappingJackson2HttpMessageConverter getJsonMessageConverter2() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SalesforceModule());
        converter.setObjectMapper(objectMapper);
        return converter;
    }

    //@Override
    protected void configureRestTemplate(RestTemplate restTemplate) {
        restTemplate.setErrorHandler(new SalesforceErrorHandler());
    }

    @Override
    public <T> List<T> readList(JsonNode jsonNode, Class<T> type) {
        CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, type);
        return (List<T>) objectMapper.convertValue(jsonNode, listType);
    }

    @Override
    public <T> T readObject(JsonNode jsonNode, Class<T> type) {
        return (T) objectMapper.convertValue(jsonNode, type);
    }

    @Override
    public String getBaseUrl() {
        return (this.instanceUrl == null ? INSTANCE_URL : this.instanceUrl) + "/services/data";
    }

    @Override
    public String getInstanceUrl() {
        return this.instanceUrl;
    }

    @Override
    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }

}
