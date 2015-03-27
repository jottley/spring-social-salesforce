package org.springframework.social.salesforce.api;

import org.springframework.social.ApiException;

/**
 * @author Umut Utkan
 */
public class InvalidIDException extends ApiException
{
    private static final long serialVersionUID = -5730717608249651423L;

    public InvalidIDException(String message)
    {
        super("Salesforce", message);
    }

}
