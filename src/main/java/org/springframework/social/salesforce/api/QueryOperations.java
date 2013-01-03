package org.springframework.social.salesforce.api;

/**
 * Defines operations for executing SOQL queries.
 *
 * @author Umut Utkan
 */
public interface QueryOperations {

    /**
     * Execute SOQL query and return the first page of results
     */
    QueryResult query(String query);

    /*
     * Retrieve next page of results based on argument from url (usually from query result "nextRecordsUrl")
     */
    QueryResult nextPage(String urlOrToken);
    
    /**
     * Execute SOQL query and return all results.
     */
    QueryResult queryAll(String query);

}
