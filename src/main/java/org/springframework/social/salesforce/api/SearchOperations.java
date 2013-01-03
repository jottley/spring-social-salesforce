package org.springframework.social.salesforce.api;

import java.util.List;

/**
 * Defines operations to execute SOSL search queries.
 *
 * @author Umut Utkan
 */
public interface SearchOperations {

    /**
     * Execute SOSL Query and retrieve results
     */
    List<ResultItem> search(String soslQuery);
    

}
