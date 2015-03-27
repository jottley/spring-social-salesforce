package org.springframework.social.salesforce.api.impl;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.social.test.client.RequestMatchers.*;
import static org.springframework.social.test.client.ResponseCreators.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.social.salesforce.api.ResultItem;

/**
 * @author Umut Utkan
 */
@Ignore
public class RecentTemplateTest extends AbstractSalesforceTest
{

    @Test
    public void search()
    {
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
