package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.ApiVersion} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiVersionMixin {

    @JsonCreator
    ApiVersionMixin(
            @JsonProperty("version") String version,
            @JsonProperty("label") String label,
            @JsonProperty("url") String url) {
    }

}
