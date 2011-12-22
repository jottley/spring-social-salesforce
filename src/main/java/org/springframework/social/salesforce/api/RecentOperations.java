package org.springframework.social.salesforce.api;

import java.util.List;

/**
 * Defines operations for interacting with Recent API.
 *
 * @author Umut Utkan
 */
public interface RecentOperations {

    List<ResultItem> recent();

}
