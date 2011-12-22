package org.springframework.social.salesforce.api;

import java.util.List;

/**
 * @author Umut Utkan
 */
public class QueryResult {

    private int totalSize;

    private boolean done;

    private String nextRecordsUrl;

    private List<ResultItem> records;


    public QueryResult(int totalSize, boolean done) {
        this.totalSize = totalSize;
        this.done = done;
    }


    public int getTotalSize() {
        return totalSize;
    }

    public boolean isDone() {
        return done;
    }

    public List<ResultItem> getRecords() {
        return records;
    }

    public void setRecords(List<ResultItem> records) {
        this.records = records;
    }

    public String getNextRecordsUrl() {
        return nextRecordsUrl;
    }

    public void setNextRecordsUrl(String nextRecordsUrl) {
        this.nextRecordsUrl = nextRecordsUrl;
    }

    public String getNextRecordsToken() {
        if (this.nextRecordsUrl != null) {
            return this.nextRecordsUrl.substring(this.nextRecordsUrl.lastIndexOf('/') + 1, this.nextRecordsUrl.length());
        }
        return null;
    }

}
