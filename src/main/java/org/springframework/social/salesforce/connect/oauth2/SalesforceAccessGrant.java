package org.springframework.social.salesforce.connect.oauth2;

import java.util.Map;

import org.springframework.social.oauth2.AccessGrant;

public class SalesforceAccessGrant extends AccessGrant
{
    private static final long serialVersionUID = -5237125582159946061L;

    private Map<String, Object> additionalInformation;

    public SalesforceAccessGrant(String accessToken,
                                 String scope,
                                 String refreshToken,
                                 Long expiresIn,
                                 Map<String, Object> additionalInformation)
    {
        super(accessToken, scope, refreshToken, expiresIn);

        this.additionalInformation = additionalInformation;
    }

    public Map<String, Object> getAdditionalInformation()
    {
        return additionalInformation;
    }
}
