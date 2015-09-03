package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
