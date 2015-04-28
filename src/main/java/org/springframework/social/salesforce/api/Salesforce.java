package org.springframework.social.salesforce.api;

import java.util.List;

import org.springframework.social.ApiBinding;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Specifies operations performed on Salesforce.
 *
 * @author Umut Utkan
 */
public interface Salesforce extends ApiBinding
{

    public ApiOperations apiOperations();

    public ChatterOperations chatterOperations();

    public QueryOperations queryOperations();

    public RecentOperations recentOperations();

    public SearchOperations searchOperations();

    public SObjectOperations sObjectsOperations();

    public <T> List<T> readList(JsonNode jsonNode,
                                Class<T> type);

    public <T> T readObject(JsonNode jsonNode,
                            Class<T> type);

    public String getBaseUrl();

    public String getInstanceUrl();

    public String getIdentityUrl();

}
