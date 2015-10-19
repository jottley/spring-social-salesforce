package org.springframework.social.salesforce.api.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.salesforce.api.ApexRestOperations;
import org.springframework.social.salesforce.api.ApiOperations;
import org.springframework.social.salesforce.api.BulkApiOperations;
import org.springframework.social.salesforce.api.ChatterOperations;
import org.springframework.social.salesforce.api.QueryOperations;
import org.springframework.social.salesforce.api.RecentOperations;
import org.springframework.social.salesforce.api.SObjectOperations;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.SearchOperations;
import org.springframework.social.salesforce.api.impl.json.SalesforceModule;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;

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

  private String identityServiceUrl;

  private ObjectMapper objectMapper;
  
  private ApexRestOperations apexRestOperations;

  private ApiOperations apiOperations;

  private BulkApiOperations bulkApiOperations;

  private ChatterOperations chatterOperations;

  private QueryOperations queryOperations;

  private RecentOperations recentOperations;

  private SearchOperations searchOperations;

  private SObjectOperations sObjectsOperations;

  public SalesforceTemplate() {
    initialize(null);
  }

  public SalesforceTemplate(String accessToken) {
    this(accessToken, null);
  }

  public SalesforceTemplate(String accessToken, String idUrl) {
    super(accessToken);
    identityServiceUrl = idUrl;
    initialize(accessToken);
    logger.debug("ACCESS TOKEN: {}", accessToken);

  }

  @Override
  protected OAuth2Version getOAuth2Version() {
    return OAuth2Version.DRAFT_10;
  }

  @Override
  public ApexRestOperations apexRestOperations() {    
    return apexRestOperations;
  }

  @Override
  public ApiOperations apiOperations() {
    return apiOperations;
  }

  @Override
  public BulkApiOperations bulkApiOperations() {
    return bulkApiOperations;
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
  
  private void initialize(String accessToken) {
    apexRestOperations = new ApexRestTemplate(this, getRestTemplate());
    apiOperations = new ApiTemplate(this, getRestTemplate());
    chatterOperations = new ChatterTemplate(this, getRestTemplate());
    queryOperations = new QueryTemplate(this, getRestTemplate());
    recentOperations = new RecentTemplate(this, getRestTemplate());
    searchOperations = new SearchTemplate(this, getRestTemplate());
    sObjectsOperations = new SObjectsTemplate(this, getRestTemplate());
    if (identityServiceUrl != null) {
      bulkApiOperations = new BulkApiTemplate(this, getRestTemplate(), accessToken);
    }
  }

  @Override
  protected MappingJackson2HttpMessageConverter getJsonMessageConverter() {
    MappingJackson2HttpMessageConverter converter = super.getJsonMessageConverter();
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
    JavaType t;
    if(type == Map.class)
      t = MapType.construct(type, SimpleType.construct(Object.class), SimpleType.construct(Object.class));
    else 
      t = SimpleType.construct(type);
    return objectMapper.convertValue(jsonNode, CollectionType.construct(List.class, t));

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

  public void setInstanceUrl(String instanceUrl) {
    this.instanceUrl = instanceUrl;
  }

  public void setIdentityServiceUrl(String idUrl) {
    this.identityServiceUrl = idUrl;
  }

  public String getIdentityServiceUrl() {
    return this.identityServiceUrl;
  }

}