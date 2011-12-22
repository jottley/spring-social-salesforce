package org.springframework.social.salesforce.api;

import java.util.List;
import java.util.Map;

/**
 * @author Umut Utkan
 */
public class Status {

    private String text;

    private List<Map<String, String>> segments;


    public Status(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Map<String, String>> getSegments() {
        return segments;
    }

    public void setSegments(List<Map<String, String>> segments) {
        this.segments = segments;
    }

    @Override
    public String toString() {
        return "Status{" +
                "text='" + text + '\'' +
                ", segments=" + segments +
                '}';
    }

}
