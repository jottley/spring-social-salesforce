package org.springframework.social.salesforce.api;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.social.ApiException;

/**
 * Encapsulates the error response sent by salesforce.
 * <p/>
 * 
 * @see <a
 *      href="http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_calls_concepts_core_data_objects.htm">
 *      Salesforce API Core Datatypes</a>
 * 
 * @author Umut Utkan
 */
public class SalesforceRequestException extends ApiException {

    private static final long serialVersionUID = 7047374539651371668L;

    private final List<String> fields;
    private final String code;

    public SalesforceRequestException(String message) {
        super(message);
        this.code = null;
        this.fields = null;
    }

    @SuppressWarnings("unchecked")
    public SalesforceRequestException(Map<String, Object> errorDetails) {
        super((String)errorDetails.get("message"));

        this.code = StringUtils.defaultString((String)errorDetails.get("errorCode"), "UNKNOWN");
        this.fields = (List<String>) errorDetails.get("fields");
    }

    public List<String> getFields() {
        return fields;
    }

    public String getCode() {
        return code;
    }

}
