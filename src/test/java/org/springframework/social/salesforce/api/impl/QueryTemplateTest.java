package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.social.salesforce.api.QueryResult;
import org.springframework.social.salesforce.api.ResultItem;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

/**
 * @author Umut Utkan
 */
public class QueryTemplateTest extends AbstractSalesforceTest {

    @Test
    public void simpleQuery() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/query?q=SELECT+Id%2C+Name%2C+BillingCity+FROM+Account"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("query-simple.json"), responseHeaders));
        QueryResult result = salesforce.queryOperations().query("SELECT Id, Name, BillingCity FROM Account");

        assertEquals(12, result.getRecords().size());
        assertEquals("/services/data/v23.0/query/01gD0000002HU6KIAW-2000", result.getNextRecordsUrl());
        assertEquals("01gD0000002HU6KIAW-2000", result.getNextRecordsToken());
        for (ResultItem item : result.getRecords()) {
            assertEquals("Account", item.getType());
        }
        Map attrFirst = result.getRecords().get(0).getAttributes();
        assertEquals("001A000000df63xIAA", attrFirst.get("Id"));
        assertEquals("GenePoint", attrFirst.get("Name"));
        assertEquals("Mountain View", attrFirst.get("BillingCity"));
    }

    @Test
    public void whereQuery() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/query?q=SELECT+Id+FROM+Contact+WHERE+Name+LIKE+%27U%25%27+AND+MailingCity+%3D+%27Istanbul%27"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("query-where.json"), responseHeaders));
        QueryResult result = salesforce.queryOperations().query("SELECT Id FROM Contact WHERE Name LIKE 'U%' AND MailingCity = 'Istanbul'");

        assertEquals(2, result.getRecords().size());
        assertEquals("Contact", result.getRecords().get(0).getType());
        assertEquals("/services/data/v23.0/sobjects/Contact/003A000000vF6QSIA0", result.getRecords().get(0).getUrl());
        assertEquals("003A000000vF6QSIA0", result.getRecords().get(0).getAttributes().get("Id"));
        assertEquals("Contact", result.getRecords().get(1).getType());
        assertEquals("/services/data/v23.0/sobjects/Contact/003A000000vF6QXIA0", result.getRecords().get(1).getUrl());
        assertEquals("003A000000vF6QXIA0", result.getRecords().get(1).getAttributes().get("Id"));
    }

    @Test
    public void child2parentQuery() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/query?q=SELECT+Contact.FirstName%2C+Contact.Account.Name+FROM+Contact"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("query-child2parent.json"), responseHeaders));
        QueryResult result = salesforce.queryOperations().query("SELECT Contact.FirstName, Contact.Account.Name FROM Contact");

        assertEquals(22, result.getRecords().size());
        for (ResultItem item : result.getRecords()) {
            assertEquals("Contact", item.getType());
        }
        assertEquals("Umut", result.getRecords().get(0).getAttributes().get("FirstName"));
        assertNull(result.getRecords().get(0).getAttributes().get("Account"));
        assertEquals("Utkan", result.getRecords().get(1).getAttributes().get("FirstName"));
        assertNull(result.getRecords().get(1).getAttributes().get("Account"));
        assertEquals("Rose", result.getRecords().get(2).getAttributes().get("FirstName"));
        ResultItem roseAccount = (ResultItem) result.getRecords().get(2).getAttributes().get("Account");
        assertEquals("Account", roseAccount.getType());
        assertEquals("/services/data/v23.0/sobjects/Account/001A000000df640IAA", roseAccount.getUrl());
        assertEquals("Edge Communications", roseAccount.getAttributes().get("Name"));
    }

    @Test
    public void countQuery() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/query?q=SELECT+COUNT%28%29+FROM+Contact"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("query-count.json"), responseHeaders));
        QueryResult result = salesforce.queryOperations().query("SELECT COUNT() FROM Contact");

        assertEquals(22, result.getTotalSize());
        assertEquals(0, result.getRecords().size());
    }

    @Test
    public void groupByQuery() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/query?q=SELECT+LeadSource%2C+COUNT%28Name%29+FROM+Lead+GROUP+BY+LeadSource"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("query-groupby.json"), responseHeaders));
        QueryResult result = salesforce.queryOperations().query("SELECT LeadSource, COUNT(Name) FROM Lead GROUP BY LeadSource");

        assertEquals(4, result.getRecords().size());
        for (ResultItem item : result.getRecords()) {
            assertEquals("AggregateResult", item.getType());
        }
        assertEquals("Web", result.getRecords().get(0).getAttributes().get("LeadSource"));
        assertEquals(7, result.getRecords().get(0).getAttributes().get("expr0"));
        assertEquals("Phone Inquiry", result.getRecords().get(1).getAttributes().get("LeadSource"));
        assertEquals(4, result.getRecords().get(1).getAttributes().get("expr0"));
    }

    @Test
    public void parent2childQuery() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/query?q=SELECT+Name%2C+%28SELECT+LastName+FROM+Contacts%29+FROM+Account"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("query-parent2child.json"), responseHeaders));
        QueryResult result = salesforce.queryOperations().query("SELECT Name, (SELECT LastName FROM Contacts) FROM Account");

        assertEquals(12, result.getRecords().size());
        for (ResultItem item : result.getRecords()) {
            assertEquals("Account", item.getType());
        }
        assertEquals("/services/data/v23.0/sobjects/Account/001A000000df63xIAA", result.getRecords().get(0).getUrl());
        assertEquals("GenePoint", result.getRecords().get(0).getAttributes().get("Name"));
        QueryResult genePointContacts = (QueryResult) result.getRecords().get(0).getAttributes().get("Contacts");
        assertEquals(1, genePointContacts.getRecords().size());
        assertEquals("Contact", genePointContacts.getRecords().get(0).getType());
        assertEquals("/services/data/v23.0/sobjects/Contact/003A000000lGE0yIAG", genePointContacts.getRecords().get(0).getUrl());
        assertEquals("Frank", genePointContacts.getRecords().get(0).getAttributes().get("LastName"));
    }

}
