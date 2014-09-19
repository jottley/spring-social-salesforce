package org.springframework.social.salesforce.api.impl;

import java.util.List;
import java.util.Map;

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
import org.springframework.social.salesforce.api.impl.json.SalesforceModule;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;

/**
 * Default implementation of Salesforce. This is the main entry point for all the operations that can be performed on Salesforce.
 *
 * @author Umut Utkan
 */
public class SalesforceTemplate extends AbstractOAuth2ApiBinding implements Salesforce {

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

	public SalesforceTemplate(final String accessToken) {
		super(accessToken);
		initialize();
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
	protected MappingJackson2HttpMessageConverter getJsonMessageConverter() {
		final MappingJackson2HttpMessageConverter converter = super.getJsonMessageConverter();
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new SalesforceModule());
		converter.setObjectMapper(objectMapper);
		return converter;
	}

	@Override
	protected void configureRestTemplate(final RestTemplate restTemplate) {
		restTemplate.setErrorHandler(new SalesforceErrorHandler());
	}
	
	@Override
	public <T> List<T> readList(final JsonNode jsonNode, final Class<T> type) {
		JavaType t;
		if(type == Map.class)
			t = MapType.construct(type, SimpleType.construct(Object.class), SimpleType.construct(Object.class));
		else 
			t = SimpleType.construct(type);
		return objectMapper.convertValue(jsonNode, CollectionType.construct(List.class, t));
//		try {
//			final CollectionType listType = TypeFactory.defaultInstance().constructCollectionType(List.class, type);
//			return (List<T>) objectMapper.readValue(jsonNode, listType);
//		} catch (final IOException e) {
//			throw new UncategorizedApiException("salesforce", "Error deserializing data from Salesforce: " + e.getMessage(), e);
//		}
	}

	@Override
	public <T> T readObject(final JsonNode jsonNode, final Class<T> type) {
		return objectMapper.convertValue(jsonNode, type);
//		try {
//			return objectMapper.readValue(jsonNode, type);
//		} catch (final IOException e) {
//			throw new UncategorizedApiException("salesforce", "Error deserializing data from Salesforce: " + e.getMessage(), e);
//		}
	}

	@Override
	public String getBaseUrl() {
		return (instanceUrl == null ? INSTANCE_URL : instanceUrl) + "/services/data";
	}

	@Override
	public String getInstanceUrl() {
		return instanceUrl;
	}

	public void setInstanceUrl(final String instanceUrl) {
		this.instanceUrl = instanceUrl;
	}

}
