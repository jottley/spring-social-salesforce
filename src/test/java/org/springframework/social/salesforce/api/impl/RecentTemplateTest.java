package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.social.salesforce.api.ResultItem;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

/**
 * @author Umut Utkan
 */
public class RecentTemplateTest extends AbstractSalesforceTest {

    @Test
    public void search() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/recent"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("recent.json"), responseHeaders));
        List<ResultItem> items = salesforce.recentOperations().recent();
        assertEquals(9, items.size());
        assertEquals("User", items.get(0).getType());
        assertEquals("/services/data/v23.0/sobjects/User/005A0000001cRuvIAE", items.get(0).getUrl());
        assertEquals("005A0000001cRuvIAE", items.get(0).getAttributes().get("Id"));
        assertEquals("Umut Utkan", items.get(0).getAttributes().get("Name"));
    }

}
