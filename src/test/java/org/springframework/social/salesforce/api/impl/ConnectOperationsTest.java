package org.springframework.social.salesforce.api.impl;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.social.salesforce.api.Community;
import org.springframework.social.salesforce.api.CommunityUser;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class ConnectOperationsTest extends AbstractSalesforceTest {

    @Test
    public void getCommunitiesList() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/connect/communities/"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.OK).body(loadResource("communities.json")).headers(responseHeaders));

        List<Community> community = salesforce.connectOperations().getCommunities();

        assertEquals("leia-community1", community.get(0).getName());
        assertEquals("leia-community2", community.get(1).getName());
        assertEquals("leia-community3", community.get(2).getName());

        assertEquals("commId1", community.get(0).getId());
        assertEquals("commId2", community.get(1).getId());
        assertEquals("commId3", community.get(2).getId());
    }

    @Test
    public void getCommunityUsersList() {
        mockServer.expect(requestTo("https://na7.salesforce.com/services/data/" + salesforce.apiOperations().getVersion() + "/connect/communities/commId1/chatter/users"))
                .andExpect(method(GET))
                .andRespond(withStatus(HttpStatus.OK).body(loadResource("community-users.json")).headers(responseHeaders));

        List<CommunityUser> communityUsers = salesforce.connectOperations().getCommunityUsers("commId1");

        assertEquals("commUserId1", communityUsers.get(0).getId());
        assertEquals("UserABCDEF", communityUsers.get(0).getCommunityNickname());
        assertEquals("mock-user@test.com", communityUsers.get(0).getUsername());
        assertEquals("MockUser1", communityUsers.get(0).getDisplayName());

    }
}
