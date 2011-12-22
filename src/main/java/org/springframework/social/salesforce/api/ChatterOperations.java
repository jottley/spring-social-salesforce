package org.springframework.social.salesforce.api;

/**
 * Defines operations for interacting with the Chatter API.
 *
 * @author Umut Utkan
 */
public interface ChatterOperations {

    /**
     * Retrieves current users's profile
     *
     * @return user profile
     */
    public SalesforceProfile getUserProfile();

    /**
     * Retrieves the given user's profile
     *
     * @param userId
     * @return user profile
     */
    public SalesforceProfile getUserProfile(String userId);

    /**
     * Retrieves current user's status
     *
     * @return status
     */
    public Status getStatus();

    /**
     * Retrieves the given user's status
     *
     * @param userId
     * @return status
     */
    public Status getStatus(String userId);

    /**
     * Updates current user's status with the given message
     *
     * @param message
     * @return status
     */
    public Status updateStatus(String message);

    /**
     * Updates the given user's status with the given message
     *
     * @param userId
     * @param message
     * @return status
     */
    public Status updateStatus(String userId, String message);

}
