package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.RecordTypeInfo} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordTypeInfoMixin {

    @JsonCreator
    RecordTypeInfoMixin(
            @JsonProperty("name") String name,
            @JsonProperty("available") boolean available,
            @JsonProperty("recordTypeId") String recordTypeId,
            @JsonProperty("defaultRecordTypeMapping") boolean defaultRecordTypeMapping) {
    }

}
