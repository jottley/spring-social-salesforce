package org.springframework.social.salesforce.api.impl.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * {@see org.springframework.social.salesforce.api.ResultItem} Mixin for api v23.0.
 *
 * @author Umut Utkan
 */
@JsonDeserialize(using = ResultItemDeserializer.class)
public class ResultItemMixin {
}
