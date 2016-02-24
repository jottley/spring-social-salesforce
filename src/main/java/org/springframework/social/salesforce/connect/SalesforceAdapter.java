package org.springframework.social.salesforce.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;
import org.springframework.social.salesforce.api.Salesforce;
import org.springframework.social.salesforce.api.SalesforceProfile;
import org.springframework.util.StringUtils;


/**
 * Salesforce ApiAdapter implementation.
 *
 * @author Umut Utkan
 * @author Jared Ottley
 */
public class SalesforceAdapter implements ApiAdapter<Salesforce> {

    private String instanceUrl = null;

    public SalesforceAdapter()
    {
        //NOOP
    }

    public SalesforceAdapter(String instanceUrl)
    {
        this.instanceUrl = instanceUrl;
    }

    public boolean test(Salesforce salesForce) {
        try {
            if (StringUtils.isEmpty(instanceUrl))
            {
                salesForce.chatterOperations().getUserProfile();
            }
            else
            {
                salesForce.chatterOperations(instanceUrl).getUserProfile();
            }
            return true;
        } catch (ApiException e) {
            return false;
        }
    }

    public void setConnectionValues(Salesforce salesforce, ConnectionValues values) {
        SalesforceProfile profile;

        if (StringUtils.isEmpty(instanceUrl))
        {
            profile = salesforce.chatterOperations().getUserProfile();
        }
        else
        {
            profile = salesforce.chatterOperations(instanceUrl).getUserProfile();
        }
        values.setProviderUserId(profile.getId());
        values.setDisplayName(profile.getFirstName() + " " + profile.getLastName());
    }

    public UserProfile fetchUserProfile(Salesforce salesforce) {
        SalesforceProfile profile;

        if (StringUtils.isEmpty(instanceUrl))
        {
            profile = salesforce.chatterOperations().getUserProfile();
        }
        else
        {
            profile = salesforce.chatterOperations(instanceUrl).getUserProfile();
        }
        return new UserProfileBuilder().setName(profile.getFirstName()).setFirstName(profile.getFirstName())
                .setLastName(profile.getLastName()).setEmail(profile.getEmail())
                .setUsername(profile.getEmail()).build();
    }


    public void updateStatus(Salesforce salesforce, String message) {
        if (StringUtils.isEmpty(instanceUrl))
        {
            salesforce.chatterOperations().updateStatus(message);
        }
        else
        {
            salesforce.chatterOperations(instanceUrl).updateStatus(message);
        }
    }
}
