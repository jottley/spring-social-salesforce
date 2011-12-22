package org.springframework.social.salesforce.api.impl.json;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.social.salesforce.api.ResultItem;

import java.util.List;

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
