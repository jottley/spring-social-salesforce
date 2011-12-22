package org.springframework.social.salesforce.api;

import java.util.List;
import java.util.Map;

/**
 * Defines operations for getting info on the API.
 *
 * @author Umut Utkan
 */
public interface ApiOperations {

    List<ApiVersion> getVersions();

    Map<String, String> getServices(String version);

}
