package org.springframework.social.salesforce.api.impl;

import org.junit.Before;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.social.test.client.MockRestServiceServer;

/**
 * @author Umut Utkan
 */
abstract public class AbstractSalesforceTest {

    protected SalesforceTemplate salesforce;

    protected SalesforceTemplate unauthorizedSalesforce;

    protected MockRestServiceServer mockServer;

    protected HttpHeaders responseHeaders;


    @Before
    public void setup() {
        salesforce = new SalesforceTemplate("ACCESS_TOKEN");
        salesforce.setInstanceUrl("https://na7.salesforce.com");
        mockServer = MockRestServiceServer.createServer(salesforce.getRestTemplate());
        responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        unauthorizedSalesforce = new SalesforceTemplate();
        // create a mock server just to avoid hitting real twitter if something gets past the authorization check
        MockRestServiceServer.createServer(unauthorizedSalesforce.getRestTemplate());
    }

    protected Resource loadResource(String name) {
        return new ClassPathResource("/" + name, getClass());
    }

}
