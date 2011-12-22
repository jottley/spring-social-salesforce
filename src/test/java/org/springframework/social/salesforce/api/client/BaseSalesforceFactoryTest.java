package org.springframework.social.salesforce.api.client;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.client.BaseSalesforceFactory;
import org.springframework.social.test.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.social.test.client.RequestMatchers.*;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

/**
 * @author Umut Utkan
 */
public class BaseSalesforceFactoryTest {

    @Test
    public void testClientLogin() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        mockServer.expect(requestTo("https://login.salesforce.com/services/oauth2/token"))
                .andExpect(method(POST))
                .andExpect(body("grant_type=password&client_id=client-id&client_secret=client-secret&username=my-username&password=my-passwordsecurity-token"))
                .andRespond(withResponse(new ClassPathResource("/client-token.json", getClass()), responseHeaders));

        Salesforce template = new BaseSalesforceFactory("client-id", "client-secret", restTemplate)
                .create("my-username", "my-password", "security-token");
        assertNotNull(template);
    }

}
