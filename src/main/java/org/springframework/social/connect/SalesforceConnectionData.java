package org.springframework.social.connect;

public class SalesforceConnectionData extends ConnectionData
{
    private static final long serialVersionUID = 444383395037815329L;

    private String identityUrl;

    public SalesforceConnectionData(String providerId,
                                    String providerUserId,
                                    String displayName,
                                    String profileUrl,
                                    String imageUrl,
                                    String accessToken,
                                    String secret,
                                    String refreshToken,
                                    Long expireTime,
                                    String identityUrl)
    {
        super(providerId,
              providerUserId,
              displayName,
              profileUrl,
              imageUrl,
              accessToken,
              secret,
              refreshToken,
              expireTime);
        this.identityUrl = identityUrl;
    }

    public String getIdentityUrl()
    {
        return identityUrl;
    }
}
