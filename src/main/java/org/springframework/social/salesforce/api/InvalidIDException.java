package org.springframework.social.salesforce.api;

import org.springframework.social.ApiException;

/**
 * @author Umut Utkan
 */
public class InvalidIDException extends ApiException {

    public InvalidIDException(String message) {
        super(message);
    }

}
