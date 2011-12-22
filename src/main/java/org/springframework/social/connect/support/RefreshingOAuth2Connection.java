package org.springframework.social.connect.support;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * @author Umut Utkan
 */
public class RefreshingOAuth2Connection<A> extends OAuth2Connection<A> {

    private String instanceUrl;


    public RefreshingOAuth2Connection(String providerId, String providerUserId, String accessToken, String refreshToken, Long expireTime, OAuth2ServiceProvider<A> aoAuth2ServiceProvider, ApiAdapter<A> aApiAdapter) {
        super(providerId, providerUserId, accessToken, refreshToken, expireTime, aoAuth2ServiceProvider, aApiAdapter);
    }

    public RefreshingOAuth2Connection(ConnectionData data, OAuth2ServiceProvider<A> aoAuth2ServiceProvider, ApiAdapter<A> aApiAdapter) {
        super(data, aoAuth2ServiceProvider, aApiAdapter);
    }



    @Override
    public A getApi() {
        if (hasExpired()) {
            refresh();
        }
        return super.getApi();
    }

}
