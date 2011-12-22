package org.springframework.social.salesforce.api;

/**
 * Defines operations for executing SOQL queries.
 *
 * @author Umut Utkan
 */
public interface QueryOperations {

    QueryResult query(String query);

    QueryResult nextPage(String urlOrToken);

}
