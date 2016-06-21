package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.social.salesforce.api.SalesforceProfile;
import org.springframework.social.salesforce.api.Status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.social.test.client.RequestMatchers.*;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

/**
 * @author Umut Utkan
 */
public class ChatterTemplateTest extends AbstractSalesforceTest {

    @Test
    public void getProfile() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/chatter/users/me"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("profile.json"), responseHeaders));
        SalesforceProfile profile = salesforce.chatterOperations().getUserProfile();
        assertEquals("Umut Utkan", profile.getName());
        assertEquals("umut.utkan@foo.com", profile.getEmail());
        assertEquals("005A0000001cRuvIAE", profile.getId());
        assertEquals("005A0000001cRuvIAE", profile.getUsername());
        assertEquals("Umut Utkan", profile.getName());
        assertEquals("https://c.na7.content.force.com/profilephoto/005/F", profile.getPhoto().getLargePhotoUrl());
        assertEquals("https://c.na7.content.force.com/profilephoto/005/T", profile.getPhoto().getSmallPhotoUrl());
    }

    @Test
    public void getStatus() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/chatter/users/me/status"))
                .andExpect(method(GET))
                .andRespond(withResponse(loadResource("chatter-status.json"), responseHeaders));

        Status status = salesforce.chatterOperations().getStatus();
        assertNotNull(status);
        assertEquals("I am also working on #hede", status.getText());
    }

    @Test
    public void updateStatus() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/v23.0/chatter/users/me/status"))
                .andExpect(method(POST))
                .andExpect(body("text=Updating+status+via+%23spring-social-salesforce%21"))
                .andRespond(withResponse(loadResource("chatter-status.json"), responseHeaders));

        Status status = salesforce.chatterOperations().updateStatus("Updating status via #spring-social-salesforce!");
        assertNotNull(status);
    }

}
