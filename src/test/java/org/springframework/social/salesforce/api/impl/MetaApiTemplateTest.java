package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.social.salesforce.api.ApiVersion;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

/**
 * @author Umut Utkan
 */
public class MetaApiTemplateTest extends AbstractSalesforceTest {

    @Test
    public void getApiVersions() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("versions.json"), responseHeaders));
        List<ApiVersion> versions = salesforce.apiOperations().getVersions();
        assertEquals(4, versions.size());
        assertEquals("Winter '12", versions.get(3).getLabel());
        assertEquals("23.0", versions.get(3).getVersion());
        assertEquals("/services/data/v23.0", versions.get(3).getUrl());
    }

    @Test
    public void getServices() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("services.json"), responseHeaders));
        Map<String, String> services = salesforce.apiOperations().getServices("23.0");
        assertEquals(6, services.size());
        assertEquals("/services/data/v23.0/sobjects", services.get("sobjects"));
        assertEquals("/services/data/v23.0/chatter", services.get("chatter"));
    }

}
