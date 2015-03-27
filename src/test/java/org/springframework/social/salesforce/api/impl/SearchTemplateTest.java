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
public class SearchTemplateTest extends AbstractSalesforceTest
{

    @Test
    public void search()
    {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/search?q=FIND+%7Bxxx*%7D+IN+ALL+FIELDS+RETURNING+Contact%2C+Account"))
                  .andExpect(method(GET))
                  .andRespond(withResponse(loadResource("search.json"), responseHeaders));
        List<ResultItem> results = salesforce.searchOperations()
                                             .search("FIND {xxx*} IN ALL FIELDS RETURNING Contact, Account");
        assertEquals(4, results.size());
        assertEquals("Contact", results.get(0).getType());
        assertEquals("Contact", results.get(1).getType());
        assertEquals("Account", results.get(2).getType());
        assertEquals("Account", results.get(3).getType());
    }

}
