package org.springframework.social.salesforce.api;

import java.io.Serializable;

/**
 * @author Umut Utkan
 */
public class ApiVersion implements Serializable {

    private String label;

    private String version;

    private String url;


    public ApiVersion(String version, String label, String url) {
        this.version = version;
        this.label = label;
        this.url = url;
    }


    public String getLabel() {
        return label;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "ApiVersion{" +
                "label='" + label + '\'' +
                ", version='" + version + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
