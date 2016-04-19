package org.springframework.social.salesforce.connect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.social.salesforce.connect.oauth2.SalesforceAccessGrant;
import org.springframework.social.support.LoggingErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * Salesforce OAuth2Template implementation.
 * <p/>
 * The reason to extend is to extract non-standard instance_url and id from Salesforce's oauth token response.
 *
 * @author Umut Utkan
 */
public class SalesforceOAuth2Template extends OAuth2Template
{
    private String instanceUrl;
    private ClientHttpRequestFactory clientHttpRequestFactory;

    public SalesforceOAuth2Template(String clientId,
                                    String clientSecret,
                                    String authorizeUrl,
                                    String accessTokenUrl,
                                    ClientHttpRequestFactory clientHttpRequestFactory)
    {
        this(clientId, clientSecret, authorizeUrl, null, accessTokenUrl, clientHttpRequestFactory);
    }

    public SalesforceOAuth2Template(String clientId,
                                    String clientSecret,
                                    String authorizeUrl,
                                    String authenticateUrl,
                                    String accessTokenUrl,
                                    ClientHttpRequestFactory clientHttpRequestFactory)
    {
        super(clientId, clientSecret, authorizeUrl, authenticateUrl, accessTokenUrl);
        setUseParametersForClientAuthentication(true);
        this.clientHttpRequestFactory = clientHttpRequestFactory;
    }

    @Override
    protected AccessGrant createAccessGrant(String accessToken,
                                            String scope,
                                            String refreshToken,
                                            Long expiresIn,
                                            Map<String, Object> response)
    {
        this.instanceUrl = (String) response.get("instance_url");

        return new SalesforceAccessGrant(accessToken, scope, refreshToken, expiresIn, response);
    }

    public String getInstanceUrl()
    {
        return instanceUrl;
    }

    @Override
    protected RestTemplate createRestTemplate()
    {
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(2);
        converters.add(new FormHttpMessageConverter());
        converters.add(new MappingJackson2HttpMessageConverter());
        converters.add(new ResourceHttpMessageConverter());
        restTemplate.setMessageConverters(converters);
        restTemplate.setErrorHandler(new LoggingErrorHandler());
        return restTemplate;
    }
}
