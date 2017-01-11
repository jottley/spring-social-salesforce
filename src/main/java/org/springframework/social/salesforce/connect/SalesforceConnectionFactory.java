package org.springframework.social.salesforce.connect;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.SalesforceConnectionData;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.support.SalesforceOAuth2Connection;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.connect.oauth2.SalesforceAccessGrant;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Umut Utkan
 */
public class SalesforceConnectionFactory extends OAuth2ConnectionFactory<Salesforce>
{
    public static final String SALESFORCE_PROVIDER_ID = "salesforce";
    public static final String SALESFORCE_SANDBOX_PROVIDER_ID = "salesforce_sandbox";

    public SalesforceConnectionFactory(String clientId,
                                       String clientSecret,
                                       ClientHttpRequestFactory clientHttpRequestFactory)
    {
        super(SALESFORCE_PROVIDER_ID,
              new SalesforceServiceProvider(clientId, clientSecret, clientHttpRequestFactory),
              new SalesforceAdapter(null));
    }

    @Override
    public Connection<Salesforce> createConnection(AccessGrant accessGrant)
    {
        return new SalesforceOAuth2Connection(getProviderIdForConnection(),
                                              extractProviderUserId(accessGrant),
                                              accessGrant.getAccessToken(),
                                              accessGrant.getRefreshToken(),
                                              accessGrant.getExpireTime(),
                                              (String) ((SalesforceAccessGrant) accessGrant).getAdditionalInformation()
                                                                                            .get("id"),
                                              getOAuth2ServiceProvider());
    }

    public String getProviderIdForConnection()
    {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        if (request.getParameter("code") != null
                && servletRequestAttributes.getAttribute(SandboxAwareSalesforceOAuth2TemplateFactory.SALESFORCE_SANDBOX_SESSION_PARAMETER,
                                                         RequestAttributes.SCOPE_SESSION) != null) {
            return SALESFORCE_SANDBOX_PROVIDER_ID;
        }
        return super.getProviderId();
    }

    @Override
    public Connection<Salesforce> createConnection(ConnectionData data)
    {
        return new SalesforceOAuth2Connection((SalesforceConnectionData) data, getOAuth2ServiceProvider());
    }

    private OAuth2ServiceProvider<Salesforce> getOAuth2ServiceProvider()
    {
        return (OAuth2ServiceProvider<Salesforce>) getServiceProvider();
    }
}
