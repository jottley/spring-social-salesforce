package org.springframework.social.salesforce.api.impl;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

import org.junit.Test;
import org.springframework.social.salesforce.api.SalesforceUserDetails;

public class UserOperationsTemplateTest extends AbstractSalesforceTest {

    @Test
    public void getUserDetails() {
        mockServer.expect(requestTo("https://login.salesforce.com/services/oauth2/userinfo"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("userDetails.json"), responseHeaders));
        SalesforceUserDetails userDetails = salesforce.userOperations().getSalesforceUserDetails();
        assertEquals("John Doe", userDetails.getName());
        assertEquals("john@doe.com", userDetails.getEmail());
        assertEquals("12345", userDetails.getId());
        assertEquals("johnny", userDetails.getPreferredUsername());
        assertEquals("John", userDetails.getFirstName());
        assertEquals("Doe", userDetails.getLastName());
    }
}
