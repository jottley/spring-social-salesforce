package org.springframework.social.salesforce.api.impl.json;

import java.util.List;

import org.springframework.social.salesforce.api.ResultItem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * {@see org.springframework.social.salesforce.api.QueryResult} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResultMixin {

    @JsonCreator
    QueryResultMixin(
            @JsonProperty("totalSize") int totalSize,
            @JsonProperty("done") boolean done) {
    }

    @JsonProperty("nextRecordsUrl")
    String nextRecordsUrl;

    @JsonProperty("records")
    List<ResultItem> records;

}
