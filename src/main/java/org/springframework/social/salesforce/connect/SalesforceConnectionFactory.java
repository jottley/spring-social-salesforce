package org.springframework.social.salesforce.connect;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.SalesforceConnectionData;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.support.SalesforceOAuth2Connection;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.connect.oauth2.SalesforceAccessGrant;

/**
 * @author Umut Utkan
 */
public class SalesforceConnectionFactory extends OAuth2ConnectionFactory<Salesforce>
{

    public SalesforceConnectionFactory(String clientId,
                                       String clientSecret)
    {
        super("salesforce", new SalesforceServiceProvider(clientId, clientSecret), new SalesforceAdapter(null));
    }

    public SalesforceConnectionFactory(String clientId,
                                       String clientSecret,
                                       String authorizeUrl,
                                       String tokenUrl)
    {
        super("salesforce",
              new SalesforceServiceProvider(clientId, clientSecret, authorizeUrl, tokenUrl),
              new SalesforceAdapter(null));
    }

    @Override
    public Connection<Salesforce> createConnection(AccessGrant accessGrant)
    {
        return new SalesforceOAuth2Connection(getProviderId(),
                                              extractProviderUserId(accessGrant),
                                              accessGrant.getAccessToken(),
                                              accessGrant.getRefreshToken(),
                                              accessGrant.getExpireTime(),
                                              (String) ((SalesforceAccessGrant) accessGrant).getAdditionalInformation()
                                                                                            .get("id"),
                                              getOAuth2ServiceProvider());
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
