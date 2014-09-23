package org.springframework.social.salesforce.api;

import org.springframework.social.ApiException;

/**
 * @author Umut Utkan
 */
@SuppressWarnings("serial")
public class InvalidIDException extends ApiException {

    public InvalidIDException(String message) {
        super("salesforce", message);
    }

}
