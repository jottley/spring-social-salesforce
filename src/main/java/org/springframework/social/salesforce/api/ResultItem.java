package org.springframework.social.salesforce.api;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class ResultItem implements Serializable {

    private String type;

    private String url;

    private Map attributes;


    public ResultItem(String type, String url) {
        this.type = type;
        this.url = url;
        this.attributes = new HashMap();
    }


    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map getAttributes() {
        return attributes;
    }

    public void setAttributes(Map attributes) {
        this.attributes = attributes;
    }

}
