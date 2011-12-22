package org.springframework.social.salesforce.api;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Umut Utkan
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo {

    private String smallPhotoUrl;

    private String largePhotoUrl;


    public Photo() {

    }


    public String getSmallPhotoUrl() {
        return smallPhotoUrl;
    }

    public void setSmallPhotoUrl(String smallPhotoUrl) {
        this.smallPhotoUrl = smallPhotoUrl;
    }

    public String getLargePhotoUrl() {
        return largePhotoUrl;
    }

    public void setLargePhotoUrl(String largePhotoUrl) {
        this.largePhotoUrl = largePhotoUrl;
    }

}
