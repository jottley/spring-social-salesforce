package org.springframework.social.salesforce.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.social.ApiBinding;

import java.util.List;

/**
 * Specifies operations performed on Salesforce.
 *
 * @author Umut Utkan
 * @author Jared Ottley
 */
public interface Salesforce extends ApiBinding {

    public ApiOperations apiOperations();

    public ApiOperations apiOperations(String instanceUrl);

    public ChatterOperations chatterOperations();

    public ChatterOperations chatterOperations(String instanceUrl);

    public QueryOperations queryOperations();

    public QueryOperations queryOperations(String instanceUrl);

    public RecentOperations recentOperations();

    public RecentOperations recentOperations(String instanceUrl);

    public SearchOperations searchOperations();

    public SearchOperations searchOperations(String instanceUrl);

    public SObjectOperations sObjectsOperations();

    public SObjectOperations sObjectsOperations(String instanceUrl);

    public <T> List<T> readList(JsonNode jsonNode, Class<T> type);

    public <T> T readObject(JsonNode jsonNode, Class<T> type);

    public String getBaseUrl();

    public String getInstanceUrl();

    public void setInstanceUrl(String instanceUrl);

}
