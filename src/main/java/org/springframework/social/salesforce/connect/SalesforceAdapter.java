package org.springframework.social.salesforce.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.SalesforceIdentity;

/**
 * Salesforce ApiAdapter implementation.
 *
 * @author Umut Utkan
 */
public class SalesforceAdapter implements ApiAdapter<Salesforce>
{
    protected String identityUrl;

    public SalesforceAdapter(String identityUrl)
    {
        this.identityUrl = identityUrl;
    }

    @Override
    public boolean test(Salesforce salesForce)
    {
        try {
            salesForce.apiOperations().getIdentity(identityUrl);
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    @Override
    public void setConnectionValues(Salesforce salesforce,
                                    ConnectionValues values)
    {
        SalesforceIdentity identity = salesforce.apiOperations().getIdentity(identityUrl);
        values.setProviderUserId(identity.getUserId());
        values.setDisplayName(identity.getFullName());
    }

    @Override
    public UserProfile fetchUserProfile(Salesforce salesforce)
    {
        SalesforceIdentity identity = salesforce.apiOperations().getIdentity(identityUrl);
        return new UserProfileBuilder().setName(identity.getFullName())
                                       .setFirstName(identity.getFirstName())
                                       .setLastName(identity.getLastName())
                                       .setEmail(identity.getEmail())
                                       .setUsername(identity.getUsername())
                                       .build();
    }

    @Override
    public void updateStatus(Salesforce salesforce,
                             String message)
    {
        salesforce.chatterOperations().updateStatus(message);
    }
}
